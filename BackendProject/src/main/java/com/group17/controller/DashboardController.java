package com.group17.controller;

import static com.group17.util.Constants.COMMON_PHRASES_AMOUNT;
import static com.group17.util.Constants.DASHBOARD_FEEDBACK_PAGE;
import static com.group17.util.Constants.DASHBOARD_FEEDBACK_PAGE_SIZE;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.group17.dashboard.DashboardEndpointType;
import com.group17.dashboard.DashboardService;
import com.group17.exception.CommonException;
import com.group17.feedback.FeedbackService;
import com.group17.negativeperday.NegativePerDayService;
import com.group17.util.LoggerUtil;

@CrossOrigin
@RestController
@RequestMapping(value = "/dashboard", produces = "application/hal+json")
public class DashboardController {
	@Autowired private DashboardService dashboardService;
	@Autowired private FeedbackService feedbackService;
	@Autowired private NegativePerDayService negativePerDayService;

	@GetMapping("{/dashboardId}")
	public ResponseEntity<?> find(@PathVariable String dashboardId) {

		Map<String, Object> map = new HashMap<String, Object>();

		for(DashboardEndpointType endpoint : DashboardEndpointType.values()) {
			String key = endpoint.getJsonKey();

			switch(endpoint) {
			case NAME:
				map.put(key, dashboardService.getDashboardById(dashboardId).getName());
				break;
			case FEEDBACK:
				map.put(key, feedbackService.getPagedFeedback(dashboardId,
															  DASHBOARD_FEEDBACK_PAGE,
															  DASHBOARD_FEEDBACK_PAGE_SIZE));
				break;
			case FEEDBACK_COUNT:
				map.put(key, feedbackService.getFeedbackCount(dashboardId));
				break;
			case FEEDBACK_SENTIMENT_COUNT:
				map.put(key, feedbackService.getSentimentCounts(dashboardId));
				break;
			case FEEDBACK_RATING_COUNT:
				map.put(key, feedbackService.getRatingCounts(dashboardId));
				break;
			case FEEDBACK_RATING_AVERAGE:
				map.put(key, feedbackService.getAverageRating(dashboardId, true));
				break;
			case FEEDBACK_RATING_NEGATIVE:
				map.put(key, negativePerDayService.findNegativePerDay(dashboardId));
				break;
			case FEEDBACK_COMMON_PHRASES:
				map.put(key, feedbackService.getCommonPhrases(dashboardId, COMMON_PHRASES_AMOUNT));
				break;
			}
		}

		// Return what has been found for debugging, etc.
		LoggerUtil.log(Level.INFO, "[Root/Dashboard] Returned " + map.size() + " values");

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
