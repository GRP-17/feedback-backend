package com.group17.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group17.FeedbackService;
import com.group17.util.CommonException;
import com.group17.util.LoggerUtil;

/**
 * Handles the root endpoint (just the app url, nothing after)
 * <p>
 * This should return a list of all the high-level endpoints in the 
 * server / children of this endpoint.
 */
@CrossOrigin
@RestController
public class RootController {
	
	@Autowired
	private FeedbackService feedbackService;

	/**
	 * There's only one endpoint, so it will one return the feedback endpoint.
	 * 
	 * @return a resource containing just a link to the all the endpoints
	 */
	@GetMapping
	public ResourceSupport index() {
		ResourceSupport rootResource = new ResourceSupport();
		rootResource.add(
			// Add 'feedback'
			linkTo(methodOn(FeedbackController.class).findAll())
				.withRel("feedback"),
				
			// Add 'feedback_count'
			linkTo(methodOn(FeedbackController.class).getCount())
				.withRel("feedback_count"),
			
			// Add 'feedback_sentiment_count'
			linkTo(methodOn(FeedbackController.class).getSentimentsCount())
				.withRel("feedback_sentiment_count"),
			

			// Add 'feedback_rating_average'
			linkTo(methodOn(FeedbackController.class).getAverageRating())
				.withRel("feedback_rating_average"),
				
			// Add 'feedback_rating_count'
			linkTo(methodOn(FeedbackController.class).getStarRatingCount())
				.withRel("feedback_rating_count"));
		return rootResource;
	}
	
	@GetMapping("/api/foos")
	public ResponseEntity<?> find(@RequestParam(name = "endpoints", required = false) 
										String[] endpoints) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> endpointsFound = new ArrayList<String>();
		List<String> endpointsNotFound = new ArrayList<String>();
		
		if(endpoints.length == 0) {
			endpoints = new String[] {"feedback", "feedback_count", "feedback_rating_average",
									  "feedback_rating_count", "feedback_sentiment_count"};
		}
		
		for(String endpoint : endpoints) {
			boolean found = true;
			
			switch(endpoint.toLowerCase()) {
			case "feedback":
				map.put(endpoint, feedbackService.getAllFeedback());
				break;
			case "feedback_count":
				map.put(endpoint, feedbackService.getCount());
				break;
			case "feedback_rating_average":
				map.put(endpoint, feedbackService.getAverageRating(true));
				break;
			case "feedback_rating_count":
				map.put(endpoint, feedbackService.getRatingCounts());
				break;
			case "feedback_sentiment_count":
				map.put(endpoint, feedbackService.getSentimentCounts());
				break;
			default:
				found = false;
				break;
			}
			
			if(found) endpointsFound.add(endpoint);
			else endpointsNotFound.add(endpoint);
		}
		
		// Return what has been found for debugging, etc.
		map.put("endpoints_found", endpointsFound);
		map.put("endpoints_not_found", endpointsNotFound);
		
		LoggerUtil.log(Level.INFO, "[Root/Dashboard] " 
										+ endpointsFound.size() + " returned, " 
										+ endpointsNotFound.size() + " not found");
		
		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(map));
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize endpoints", 
									  HttpStatus.NO_CONTENT.value());
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
