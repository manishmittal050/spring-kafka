package com.reqres.api;



import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.reqres.api.entity.Book;
import com.reqres.api.entity.LibraryEvent;

import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {"library-events"}, partitions = 3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
		"spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
class LibraryEventIntegrationTest {
	
	
	@Autowired
	TestRestTemplate restTemplate;
	
	@Autowired
	EmbeddedKafkaBroker broker;
	
	private Consumer<Integer, String> consumer;
	
	@BeforeEach
	void setup() {
		Map<String, Object> map = new HashMap<>(KafkaTestUtils.consumerProps("group1", "true", broker));
		consumer = new DefaultKafkaConsumerFactory<>(map, new IntegerDeserializer(), new StringDeserializer()).createConsumer();
		broker.consumeFromAllEmbeddedTopics(consumer);
	}

	
	@AfterEach
	void tearDown() {
		consumer.close();
	}
	
	@Test
	void postLibraryEvent() {
		
		LibraryEvent libraryEvent = new LibraryEvent();
		
		Book book = new Book();
		book.setBookAuthor("MANISH");
		book.setBookId(123);
		book.setBookName("MICROSERVICES");
		libraryEvent.setBook(book);
		libraryEvent.setLibraryEventId(null);
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", MediaType.APPLICATION_JSON.toString());
		
		HttpEntity<LibraryEvent> entity = new HttpEntity<LibraryEvent>(libraryEvent, headers);
	
		ResponseEntity<LibraryEvent> responseEntity= restTemplate.exchange("/v1/libraryevent", HttpMethod.POST,entity, LibraryEvent.class);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		
		ConsumerRecord<Integer, String> consumerRecord= KafkaTestUtils.getSingleRecord(consumer, "library-events");
		String expected = "{\"libraryEventId\":null,\"book\":{\"bookId\":123,\"bookName\":\"MICROSERVICES\",\"bookAuthor\":\"MANISH\"}}";
		String value = consumerRecord.value();
		assertEquals(expected, value);
	}
	
	

}
