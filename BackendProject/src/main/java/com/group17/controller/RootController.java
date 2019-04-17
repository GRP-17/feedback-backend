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
	private static final String DUMMY_ID = null;
	/** A dummy query to be trimmed from the links. */
	private static final String DUMMY_QUERY = null;
	/** A dummy timestamp to be trimmed from the links. */
	private static final long DUMMY_SINCE = Long.MAX_VALUE;
	/** A dummy sentiment to be trimmed from the links. */
	private static final String DUMMY_SENTIMENT = null;
	
	private static final int DUMMY_PAGE = 0;
	private static final int DUMMY_PAGE_SIZE = 0;

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
										.findFeedback(DUMMY_ID, DUMMY_PAGE, 
													  DUMMY_PAGE_SIZE, DUMMY_QUERY,  
													  DUMMY_SINCE, DUMMY_SENTIMENT))
								.withRel("feedback")),
			
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.stats(DUMMY_ID, DUMMY_PAGE, 
											   DUMMY_PAGE_SIZE, DUMMY_QUERY, 
											   DUMMY_SINCE, DUMMY_SENTIMENT))
								.withRel("feedback_stats")),
				
			// Add 'feedback_count'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getCount(DUMMY_ID, DUMMY_QUERY, 
												  DUMMY_SINCE, DUMMY_SENTIMENT))
								.withRel("feedback_count")),
			
			// Add 'feedback_sentiment_count'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getSentimentsCount(DUMMY_ID, DUMMY_QUERY, 
												   			DUMMY_SINCE, DUMMY_SENTIMENT))
								.withRel("feedback_sentiment_count")),

			// Add 'feedback_rating_average'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getAverageRating(DUMMY_ID, DUMMY_QUERY, 
												   		  DUMMY_SINCE, DUMMY_SENTIMENT))
								.withRel("feedback_rating_average")),
				
			// Add 'feedback_rating_count'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getStarRatingCount(DUMMY_ID, DUMMY_QUERY, 
												   			DUMMY_SINCE, DUMMY_SENTIMENT))
								.withRel("feedback_rating_count")),
				
			// Add 'feedback_rating_negative'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getNegativePerDay(DUMMY_ID, DUMMY_QUERY, 
												   		   DUMMY_SINCE, DUMMY_SENTIMENT))
								.withRel("feedback_rating_negative")),
				
			// Add 'feedback_common_phrases'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getCommonPhrases(DUMMY_ID, DUMMY_QUERY, 
												   		 DUMMY_SINCE, DUMMY_SENTIMENT))
								.withRel("feedback_common_phrases")),

			// Add 'dashboards'
			removeParameters(linkTo(methodOn(DashboardsController.class)
										.findAll())
								.withRel("dashboards")),

			// Add 'labels'
			removeParameters(linkTo(methodOn(LabelsController.class)
										.findAll())
								.withRel("labels")));
		return rootResource;
	}

}
