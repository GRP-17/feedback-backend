package com.group17.controller;

import static com.group17.util.RelBuilder.newInstance;
import static com.group17.util.RelBuilder.LinkType.COUNT;
import static com.group17.util.RelBuilder.LinkType.RATING_AVERAGE;
import static com.group17.util.RelBuilder.LinkType.RATING_COUNT;
import static com.group17.util.RelBuilder.LinkType.ROOT;
import static com.group17.util.RelBuilder.LinkType.FINDALL;
import static com.group17.util.RelBuilder.LinkType.SENTIMENT_COUNT;
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
				.withRel(newInstance(ROOT).withPrefix("feedback").build()),
				
			// Add 'feedback_findall'
			linkTo(methodOn(FeedbackController.class).findAll())
				.withRel(newInstance(FINDALL).withPrefix("feedback").build()),
				
			// Add 'feedback_count'
			linkTo(methodOn(FeedbackController.class).getCount())
				.withRel(newInstance(COUNT).withPrefix("feedback").build()),
			
			// Add 'feedback_sentiment_count'
			linkTo(methodOn(FeedbackController.class).getSentimentsCount())
				.withRel(newInstance(SENTIMENT_COUNT).withPrefix("feedback").build()),
			

			// Add 'feedback_rating_average'
			linkTo(methodOn(FeedbackController.class).getAverageRating())
				.withRel(newInstance(RATING_AVERAGE).withPrefix("feedback").build()),
				
			// Add 'feedback_rating_count'
			linkTo(methodOn(FeedbackController.class).getStarRatingCount())
				.withRel(newInstance(RATING_COUNT).withPrefix("feedback").build()));
		return rootResource;
	}
	
}
