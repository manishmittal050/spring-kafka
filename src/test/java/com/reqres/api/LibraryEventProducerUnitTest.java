package com.reqres.api;


import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.isA;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reqres.api.entity.Book;
import com.reqres.api.entity.LibraryEvent;
import com.reqres.api.producer.LibraryEventProducer;

@ExtendWith(MockitoExtension.class)
public class LibraryEventProducerUnitTest {

	@Mock
	KafkaTemplate<Integer, String> kafkaTemplate;
	
	
	@Spy
	ObjectMapper objectMapper;
	
	@InjectMocks
	LibraryEventProducer libraryEventProducer;
	
	@Test
	void sendLibraryApproach_2_failure() throws JsonProcessingException {
		LibraryEvent libraryEvent = new LibraryEvent();
		Book book = new Book();
		book.setBookAuthor("MANISH");
		book.setBookId(123);
		book.setBookName("MICROSERVICES");
		libraryEvent.setBook(book);
		libraryEvent.setLibraryEventId(null);
		
		SettableListenableFuture future = new SettableListenableFuture();
		future.setException(new RuntimeException("Exception calling kafka..."));
		
		when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
		
//		libraryEventProducer.sendLibraryEvent_approach_2(libraryEvent).get();
		assertThrows(Exception.class, ()->libraryEventProducer.sendLibraryEvent_approach_2(libraryEvent).get());
		
	}

	
	 @Test
	    void sendLibraryEvent_Approach2_success() throws JsonProcessingException, ExecutionException, InterruptedException {
	        //given
		 LibraryEvent libraryEvent = new LibraryEvent();
			Book book = new Book();
			book.setBookAuthor("MANISH");
			book.setBookId(123);
			book.setBookName("MICROSERVICES");
			libraryEvent.setBook(book);
			libraryEvent.setLibraryEventId(null);
			
	        String record = objectMapper.writeValueAsString(libraryEvent);
	        SettableListenableFuture future = new SettableListenableFuture();

	        ProducerRecord<Integer, String> producerRecord = new ProducerRecord("library-events", libraryEvent.getLibraryEventId(),record );
	        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition("library-events", 1),
	                1,1,342,System.currentTimeMillis(), 1, 2);
	        SendResult<Integer, String> sendResult = new SendResult<Integer, String>(producerRecord,recordMetadata);

	        future.set(sendResult);
	        when(kafkaTemplate.send(isA(ProducerRecord.class))).thenReturn(future);
	        //when

	        ListenableFuture<SendResult<Integer,String>> listenableFuture =  libraryEventProducer.sendLibraryEvent_approach_2(libraryEvent);

	        //then
	        SendResult<Integer,String> sendResult1 = listenableFuture.get();
	        assert sendResult1.getRecordMetadata().partition()==1;

	    }

	
	
}
