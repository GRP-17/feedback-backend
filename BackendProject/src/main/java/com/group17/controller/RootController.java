package com.group17.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
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
	 * A dummy dashboardId that will be trimmed from the links given
	 * to the frontend.
	 */
	private static final String DASHBOARD_ID = null;

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
										.findAll(DASHBOARD_ID))
								.withRel("feedback")),
				
			// Add 'feedback_paged'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getPaged(DASHBOARD_ID, 0, 0))
								.withRel("feedback_paged")),
				
			// Add 'feedback_count'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getCount(DASHBOARD_ID))
								.withRel("feedback_count")),
			
			// Add 'feedback_sentiment_count'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getSentimentsCount(DASHBOARD_ID))
								.withRel("feedback_sentiment_count")),

			// Add 'feedback_rating_average'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getAverageRating(DASHBOARD_ID))
								.withRel("feedback_rating_average")),
				
			// Add 'feedback_rating_count'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getStarRatingCount(DASHBOARD_ID))
								.withRel("feedback_rating_count")),
				
			// Add 'feedback_rating_negative'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getNegativePerDay(DASHBOARD_ID))
								.withRel("feedback_rating_negative")),
				
			// Add 'feedback_common_phrases'
			removeParameters(linkTo(methodOn(FeedbackController.class)
										.getCommonPhrases(DASHBOARD_ID))
								.withRel("feedback_common_phrases")),

			// Add 'dashboard' as self relation - the values don't matter
			removeParameters(linkTo(methodOn(DashboardController.class)
										.find(DASHBOARD_ID))
								.withRel("dashboard")));
		return rootResource;
	}
	
	private Link removeParameters(Link link) {
		String ref = link.getHref();
		if(ref.contains("{?")) {
			return new Link(ref.substring(0, ref.indexOf('{')), link.getRel());
		} else {
			if(ref.contains("?")) {
				return new Link(ref.substring(0, ref.indexOf('?')), link.getRel());
			} else {
				return link;
			}
		}
	}

}
