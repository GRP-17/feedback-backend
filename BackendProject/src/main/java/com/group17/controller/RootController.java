package com.group17.controller;

import static com.group17.util.LinkUtil.removeParameters;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles the root endpoint (just the app url, nothing after)
 * <p>
 * This should return a list of all the high-level endpoints in the 
 * server / children of this endpoint.
 */
@CrossOrigin
@RestController
public class RootController {
	/** A dummy dashboardId to be trimmed from the links. */
	private static final String DASHBOARD_ID = null;
	/** A dummy query to be trimmed from the links. */
	private static final String DASHBOARD_QUERY = null;
	/** A dummy timestamp to be trimmed from the links. */
	private static final long DASHBOARD_SINCE = Long.MAX_VALUE;
	/** A dummy sentiment to be trimmed from the links. */
	private static final String DASHBOARD_SENTIMENT = null;

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
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.findAll(DASHBOARD_ID, DASHBOARD_QUERY, 
												 DASHBOARD_SINCE, DASHBOARD_SENTIMENT))
								.withRel("feedback")),
			
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.stats(DASHBOARD_ID, DASHBOARD_QUERY, 
											   DASHBOARD_SINCE, DASHBOARD_SENTIMENT))
								.withRel("feedback_stats")),
				
			// Add 'feedback_paged'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getPaged(DASHBOARD_ID, 0, 0, DASHBOARD_QUERY, 
												  DASHBOARD_SINCE, DASHBOARD_SENTIMENT))
								.withRel("feedback_paged")),
				
			// Add 'feedback_count'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getCount(DASHBOARD_ID, DASHBOARD_QUERY, 
												  DASHBOARD_SINCE, DASHBOARD_SENTIMENT))
								.withRel("feedback_count")),
			
			// Add 'feedback_sentiment_count'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getSentimentsCount(DASHBOARD_ID, DASHBOARD_QUERY, 
												   			DASHBOARD_SINCE, DASHBOARD_SENTIMENT))
								.withRel("feedback_sentiment_count")),

			// Add 'feedback_rating_average'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getAverageRating(DASHBOARD_ID, DASHBOARD_QUERY, 
												   		  DASHBOARD_SINCE, DASHBOARD_SENTIMENT))
								.withRel("feedback_rating_average")),
				
			// Add 'feedback_rating_count'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getStarRatingCount(DASHBOARD_ID, DASHBOARD_QUERY, 
												   			DASHBOARD_SINCE, DASHBOARD_SENTIMENT))
								.withRel("feedback_rating_count")),
				
			// Add 'feedback_rating_negative'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getNegativePerDay(DASHBOARD_ID, DASHBOARD_QUERY, 
												   		   DASHBOARD_SINCE, DASHBOARD_SENTIMENT))
								.withRel("feedback_rating_negative")),
				
			// Add 'feedback_common_phrases'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getCommonPhrases(DASHBOARD_ID, DASHBOARD_QUERY, 
												   		 DASHBOARD_SINCE, DASHBOARD_SENTIMENT))
								.withRel("feedback_common_phrases")),

			// Add 'dashboard_findall'
			removeParameters(linkTo(methodOn(DashboardsController.class)
										.findAll())
								.withRel("dashboards")));
		return rootResource;
	}

}
