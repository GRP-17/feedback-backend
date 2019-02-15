package com.group17.feedback.day;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Used as a factory for the Resources
 */
@Component
public class DayResourceAssembler implements ResourceAssembler<Day, Resource<Day>> {

	@Override
	public Resource<Day> toResource(Day day) {
		return new Resource<>(
				day/*,
				linkTo(methodOn(FeedbackController.class).findOne(feedback.getId())).withSelfRel(),
				linkTo(methodOn(FeedbackController.class).findAll()).withRel("feedback")*/);
	}
}
