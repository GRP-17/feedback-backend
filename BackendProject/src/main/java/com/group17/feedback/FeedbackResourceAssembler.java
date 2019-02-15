package com.group17.feedback;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.group17.controller.FeedbackController;

/**
 * Used as a factory for the Resources
 */
@Component
public class FeedbackResourceAssembler implements ResourceAssembler<Feedback, Resource<Feedback>> {

	/**
	 * Generates a resource from the Feedback object supplied
	 * @param feedback the object to make into a resource
	 * @return a resource object wrapping up the feedback, has links to the feedback itself and the endpoint
	 */
	@Override
	public Resource<Feedback> toResource(Feedback feedback) {
		return new Resource<>(
				feedback,
				linkTo(methodOn(FeedbackController.class).findOne(feedback.getId())).withSelfRel(),
				linkTo(methodOn(FeedbackController.class).findAll()).withRel("feedback"));
	}
}
