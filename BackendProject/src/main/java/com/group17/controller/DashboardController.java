package com.group17.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group17.FeedbackService;
import com.group17.util.CommonException;
import com.group17.util.LoggerUtil;
import com.group17.util.RelBuilder;
import com.group17.util.RelBuilder.LinkType;

@CrossOrigin
@RestController
@RequestMapping(value = "/dashboard", produces = "application/hal+json")
public class DashboardController {
	
	@Autowired
	private FeedbackService feedbackService;
	
	@GetMapping()
	@ResponseBody
	public ResponseEntity<?> getAll() {
		LinkType[] links = LinkType.values();
		String[] endpoints = new String[links.length];
		
		for(int i = 0; i < links.length; i ++) {
			endpoints[i] = getRel(links[i]);
		}
		
		return getSelected(endpoints);
	}

	@GetMapping()
	@ResponseBody
	public ResponseEntity<?> getSelected(@PathVariable String[] endpoints) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		for(String endpoint : endpoints) {
			// Determine which LinkType it is
			inner: for(LinkType type : LinkType.values()) {
				if(endpoint.contains(type.getPath().toLowerCase())) {
					
					switch(type) {
					case COUNT:
						map.put(endpoint, feedbackService.getCount());
						break;
						
					case FINDALL:
						map.put(endpoint, feedbackService.getAllFeedback());
						break;
						
					case RATING_AVERAGE:
						map.put(endpoint, feedbackService.getAverageRating(true));
						break;
						
					case RATING_COUNT:
						map.put(endpoint, feedbackService.getRatingCounts());
						break;
						
					case SENTIMENT_COUNT:
						map.put(endpoint, feedbackService.getSentimentCounts());
						break;
						
					default:
					case ROOT:
						// We ignore ROOT - it doesn't return any useful data for the dashboard
						break;
					}
					break inner;
				}
			}
		}
		
		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(map));
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize endpoints", HttpStatus.NO_CONTENT.value());
		}
	}
	
	private String getRel(LinkType type) {
		return RelBuilder.newInstance(type).withPrefix("feedback").build();
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
