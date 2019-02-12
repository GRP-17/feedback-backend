package com.group17.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group17.util.CommonException;
import com.group17.util.LoggerUtil;

@CrossOrigin
@RestController
@RequestMapping(value = "/dashboard", produces = "application/hal+json")
public class DashboardController {
	
	@GetMapping()
	public ResponseEntity<?> getAll() {
//		linkTo(methodOn(FeedbackController.class).findAll()).withRel("feedback"),
//		linkTo(methodOn(FeedbackController.class).getCount()).withRel("feedback_count"),
//		linkTo(methodOn(FeedbackController.class).getSentimentsCount()).withRel("feedback_sentiment_count"),
//		linkTo(methodOn(FeedbackController.class).getAverageRating()).withRel("feedback_rating_average"),
//		linkTo(methodOn(FeedbackController.class).getStarRatingCount()).withRel("feedback_rating_count"));
		
		FeedbackController inst = methodOn(FeedbackController.class);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("feedback", inst.findAll().getContent().toString());
		map.put("feedback_count", inst.getCount().getBody());
		
		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(map));
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize endpoints", HttpStatus.NO_CONTENT.value());
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<String> findOne(@PathVariable String id) {
		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(id + " Test"));
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize endpoints", HttpStatus.NO_CONTENT.value());
		}
	}
	
	/**
	 * Handles any CommonExceptions thrown.
	 *
	 * @param ex the exception that was thrown
	 * @return a response entity with the message and HTTP status code
	 */
	/* Error Handlers */
	@ExceptionHandler(CommonException.class)
	public ResponseEntity<String> exceptionHandler(CommonException ex) {
		LoggerUtil.logException(ex);
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.valueOf(ex.getErrorCode()));
	}

}
