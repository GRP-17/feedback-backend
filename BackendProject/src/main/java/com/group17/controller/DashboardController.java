package com.group17.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group17.FeedbackService;
import com.group17.util.CommonException;
import com.group17.util.LoggerUtil;

@CrossOrigin
@RestController
@RequestMapping(value = "/dashboard", produces = "application/hal+json")
public class DashboardController {
	
	@Autowired
	private FeedbackService feedbackService;
//	
//	@GetMapping()
//	@ResponseBody
//	public ResponseEntity<?> getAll() {
//		return find(new String[]{"feedback_count", "feedback_rating_average"});
//	}

	@RequestMapping(value="/find/{endpoints}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> find(@PathVariable String[] endpoints) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		for(String endpoint : endpoints) {
			// Determine which LinkType it is
			switch(endpoint.toLowerCase()) {
			case "feedback_count":
				map.put(endpoint, feedbackService.getCount());
				break;
			case "feedback_rating_average":
				map.put(endpoint, feedbackService.getAverageRating(true));
				break;
			}
//					switch(type) {
//					case COUNT:
//						map.put(endpoint, feedbackService.getCount());
//						break;
//						
//					case RATING_AVERAGE:
//						map.put(endpoint, feedbackService.getAverageRating(true));
//						break;
//						
//					case RATING_COUNT:
//						map.put(endpoint, feedbackService.getRatingCounts());
//						break;
//						
//					case SENTIMENT_COUNT:
//						map.put(endpoint, feedbackService.getSentimentCounts());
//						break;
//						
//					default:
//					case ROOT:
//						// We ignore ROOT - it doesn't return any useful data for the dashboard
//						break;
//					}
//					break inner;
//				}
//			}
		}
		
		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(map));
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
