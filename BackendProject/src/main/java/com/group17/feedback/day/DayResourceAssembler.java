package com.group17.feedback.day;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Used as a factory for the Resources
 */
@Component
public class DayResourceAssembler implements ResourceAssembler<Day, Resource<Day>> {
	/**
	 * Generates a resource from the Day object supplied
	 * @param day the object to make into a resource
	 * @return a resource object wrapping up the feedback, has links to the feedback itself and the endpoint
	 */

	@Override
	public Resource<Day> toResource(Day day) {
		return new Resource<Day>(day);
	}
	
}