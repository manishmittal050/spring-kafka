package com.reqres.api;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.reqres.api.controlllers.RestReqController;
import com.reqres.api.entity.Book;
import com.reqres.api.entity.LibraryEvent;

@WebMvcTest(RestReqController.class)
@AutoConfigureMockMvc
public class LibraryEventControllerUnitTest {

	
	@Autowired
	MockMvc mockMvc;
	
	
	@Test
	void postLibraryEvent() {
		LibraryEvent libraryEvent = new LibraryEvent();
		Book book = new Book();
		book.setBookAuthor("MANISH");
		book.setBookId(123);
		book.setBookName("MICROSERVICES");
		libraryEvent.setBook(book);
		libraryEvent.setLibraryEventId(null);
		
//		mockMvc.perform(post(""/v1/libraryevent").content())
		
	}
	
}
