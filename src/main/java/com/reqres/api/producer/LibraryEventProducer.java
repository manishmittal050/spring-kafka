package com.reqres.api.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reqres.api.entity.LibraryEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LibraryEventProducer {
	
	Logger logger = LoggerFactory.getLogger(LibraryEventProducer.class);
	
	String topic = "library-events";
	
	@Autowired
	private KafkaTemplate<Integer, String> kafkaTemplate;
	
	@Autowired
	private ObjectMapper mapper;
	
	public void sendLibraryEvent(LibraryEvent event) throws JsonProcessingException {
		Integer key = event.getLibraryEventId();
		String value = mapper.writeValueAsString(event);
		
		
	    ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.sendDefault(key, value);
	    
	    listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				handleSuccess(key,value,result);
				
			}

			@Override
			public void onFailure(Throwable ex) {
				handleFailure(key,value,ex);
				
			}
		});
	}

	
	public void sendLibraryEvent_approach_2(LibraryEvent event) throws JsonProcessingException {
		Integer key = event.getLibraryEventId();
		String value = mapper.writeValueAsString(event);
		
		ProducerRecord<Integer, String> producerRecord = buildProducerRecord(key,value,topic);
	    ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.send(producerRecord);
	    
	    listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				handleSuccess(key,value,result);
				
			}

			@Override
			public void onFailure(Throwable ex) {
				handleFailure(key,value,ex);
				
			}
		});
	}

	
	
	private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic2) {
		// TODO Auto-generated method stub
		return new ProducerRecord<Integer, String>(topic2, null, key, value, null);
	}


	protected void handleFailure(Integer key, String value, Throwable ex) {
		logger.info("Error sending the message and exception is {}", ex.getMessage());
		try {
			throw ex;
		}catch (Throwable throwable) {
			logger.error("Error in failure {} ", throwable.getMessage());
		}
		
	}

	protected void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
		logger.info("Message sent successfully from key {} and the value is : {}, partition is : {}", key, value, result.getRecordMetadata().partition());
		
	}
	
	

}
