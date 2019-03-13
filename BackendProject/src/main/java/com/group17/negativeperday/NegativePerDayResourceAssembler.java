package com.group17.negativeperday;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


/**
 * Used as a factory for the Resources
 */
@Component
public class NegativePerDayResourceAssembler implements ResourceAssembler<NegativePerDay, Resource<NegativePerDay>> {
	/**
	 * Generates a resource from the NegativePerDay object supplied
	 *
	 * @param negativePerDay the object to make into a resource
	 * @return a resource object wrapping up the negativePerDay, has links to the feedback itself and the endpoint
	 */

	@Override
	public Resource<NegativePerDay> toResource(NegativePerDay negativePerDay) {
		return new Resource<>(
				negativePerDay,
				linkTo(methodOn(NegativePerDayService.class).findNegativePerDay()).withRel("negative_per_day"));
	}

}