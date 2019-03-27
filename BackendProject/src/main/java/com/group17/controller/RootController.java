package com.group17.controller;

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
				
			// Add 'feedback_paged'
			linkTo(methodOn(FeedbackController.class).getPaged(0, 0))
				.withRel("feedback_paged"),
				
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
				.withRel("feedback_rating_count"),
				
			// Add 'feedback_rating_negative'
			linkTo(methodOn(FeedbackController.class).getNegativePerDay())
				.withRel("feedback_rating_negative"),
				
			// Add 'feedback_common_phrases'
			linkTo(methodOn(FeedbackController.class).getCommonPhrases())
				.withRel("feedback_common_phrases"),


			// Add 'dashboard'
			linkTo(methodOn(DashboardController.class).find(null))
				.withSelfRel().expand().withRel("dashboard"));
		return rootResource;
	}
	
}
