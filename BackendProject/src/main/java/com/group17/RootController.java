package com.group17;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Handles the root endpoint (just the app url, nothing after)
 * should return a list of all the high-level endpoints in the server / children of this endpoint
 */
@CrossOrigin
@RestController
public class RootController {


	/**
	 * Only have one endpoint, so it will one return the feedback endpoint
	 * @return a resource containing just a link to the all the endpoints
	 */
	@GetMapping
	public ResourceSupport index() {
		ResourceSupport rootResource = new ResourceSupport();
		rootResource.add(linkTo(methodOn(FeedbackController.class).findAll()).withRel("feedback"));
		return rootResource;
	}
	
}
