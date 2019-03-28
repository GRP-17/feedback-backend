package com.group17.controller;

import static com.group17.util.Constants.COMMON_PHRASES_AMOUNT;
import static com.group17.util.Constants.DASHBOARD_FEEDBACK_PAGE;
import static com.group17.util.Constants.DASHBOARD_FEEDBACK_PAGE_SIZE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group17.dashboard.Dashboard;
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

	@GetMapping()
	public Resources<Resource<Dashboard>> findAll() throws CommonException {
		List<Resource<Dashboard>> resources = dashboardService.getAllDashboards();
		LoggerUtil.log(Level.INFO, "[Dashboard/Retrieve] Retrieved: " 
										+ resources.size() + " dashboards");
		return new Resources<Resource<Dashboard>>(
				resources,
				linkTo(methodOn(DashboardController.class).findAll())
					.withSelfRel(),
				linkTo(methodOn(DashboardController.class).find(null))
					.withRel("find"));
	}
	
	@PostMapping(headers = "Accept=application/json")
	public ResponseEntity<?> create(@RequestBody Dashboard newDashboard)
			throws URISyntaxException, TransactionSystemException {
		Resource<Dashboard> resource = dashboardService.createDashboard(newDashboard);

		LoggerUtil.log(Level.INFO, "[Dashboard/Create] Created: " + newDashboard.getId());
		return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
	}
	
	@PutMapping("/{dashboardId}")
	public ResponseEntity<?> update(@RequestBody Dashboard newDashboard, @PathVariable String dashboardId)
			throws URISyntaxException, TransactionSystemException {

		Resource<Dashboard> resource = dashboardService.updateDashboard(dashboardId, newDashboard);
		LoggerUtil.log(Level.INFO, "[Feedback/Update] Updated: " + dashboardId
										+ ". Object: " + newDashboard.toString());
		return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
	}

	@DeleteMapping("/{dashboardId}")
	public ResponseEntity<?> delete(@PathVariable String dashboardId) {

		try {
			dashboardService.deleteDashboardById(dashboardId);
			LoggerUtil.log(Level.INFO, "[Dashboard/Delete] Deleted: " + dashboardId);
		} catch (Exception e) {
		}

		return ResponseEntity.noContent().build();
	}
	
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
