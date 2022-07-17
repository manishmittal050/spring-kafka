package com.reqres.api.controlllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reqres.api.entity.LibraryEvent;
import com.reqres.api.producer.LibraryEventProducer;
import com.reqres.api.services.UserService;

@RestController
//@RequestMapping("restreq")
public class RestReqController {
	
//	@Autowired 
//	private UserService service;
	
//	@GetMapping("/api/users/{id}")
//	public ResponseEntity<String> getUserById(@PathVariable int id) {
//		String email = service.fetchEmailById(id);
//		if(null==email) {
//			return new ResponseEntity<String>("No Data Found", HttpStatus.NOT_FOUND);
//		}
//		
//		return new ResponseEntity<String>(email,HttpStatus.OK);
//	}
	
	@Autowired
	LibraryEventProducer libraryEventProducer;
	
	
	@RequestMapping(value = "v1/libraryevent", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException{
		
//		libraryEventProducer.sendLibraryEvent(libraryEvent);
		libraryEventProducer.sendLibraryEvent_approach_2(libraryEvent);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
	}
	

}
