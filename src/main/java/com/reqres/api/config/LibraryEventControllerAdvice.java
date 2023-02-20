package com.reqres.api.config;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class LibraryEventControllerAdvice {
	
	Logger logger = LoggerFactory.getLogger(LibraryEventControllerAdvice.class);
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleRequestBody(MethodArgumentNotValidException ex){
		List<FieldError> list = ex.getFieldErrors();
		String errorMessage = list.stream()
				.map(er -> er.getField()+" - "+ er.getDefaultMessage())
				.collect(Collectors.joining(", "));

		logger.info("ErrorMessage is {} ", errorMessage);
		
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
		
	}

}
