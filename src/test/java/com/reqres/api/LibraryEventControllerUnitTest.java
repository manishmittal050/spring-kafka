package com.reqres.api;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.Strategy.Content;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reqres.api.controlllers.RestReqController;
import com.reqres.api.entity.Book;
import com.reqres.api.entity.LibraryEvent;
import com.reqres.api.producer.LibraryEventProducer;

@WebMvcTest(RestReqController.class)
@AutoConfigureMockMvc
public class LibraryEventControllerUnitTest {

	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	LibraryEventProducer libraryEventProducer;
	
	
	ObjectMapper mapper = new ObjectMapper();
	
	@Test
	void postLibraryEvent() throws Exception {
		LibraryEvent libraryEvent = new LibraryEvent();
		Book book = new Book();
		book.setBookAuthor("MANISH");
		book.setBookId(123);
		book.setBookName("MICROSERVICES");
		libraryEvent.setBook(book);
		libraryEvent.setLibraryEventId(null);
		
		String json = mapper.writeValueAsString(libraryEvent);
		
		doNothing().when(libraryEventProducer).sendLibraryEvent_approach_2(isA(LibraryEvent.class));
		
		mockMvc.perform(post("/v1/libraryevent")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated());
		
	}
	
	
	
	@Test
	void postLibraryEvent_4xx() throws Exception {
		LibraryEvent libraryEvent = new LibraryEvent();
		Book book = new Book();
		book.setBookAuthor("MANISH");
//		book.setBookId(123);
//		book.setBookName("MICROSERVICES");
		libraryEvent.setBook(book);
		libraryEvent.setLibraryEventId(null);
		
		String json = mapper.writeValueAsString(libraryEvent);
		
//		doNothing().when(libraryEventProducer).sendLibraryEvent_approach_2(isA(LibraryEvent.class));
		
		when(libraryEventProducer.sendLibraryEvent_approach_2(isA(LibraryEvent.class))).thenReturn(null);

        String expectedErrorMessage = "book.bookAuthor - must not be blank, book.bookId - must not be null";
		
		mockMvc.perform(post("/v1/libraryevent")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().is4xxClientError())
					.andExpect(content().string(expectedErrorMessage));
		
	}
	
	
}
