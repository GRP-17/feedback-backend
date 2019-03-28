package com.group17.dashboard;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.group17.controller.DashboardController;

/**
 * Used as a factory for the Resources
 */
@Component
public class DashboardResourceAssembler implements ResourceAssembler<Dashboard, Resource<Dashboard>> {

	@Override
	public Resource<Dashboard> toResource(Dashboard dashboard) {
		return new Resource<>(
				dashboard,
				linkTo(methodOn(DashboardController.class)
							.find(dashboard.getId()))
					.withSelfRel());
	}
}
