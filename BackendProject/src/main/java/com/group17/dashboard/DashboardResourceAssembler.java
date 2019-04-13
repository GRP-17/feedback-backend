package com.group17.dashboard;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Used as a factory for the Resources
 */
@Component
public class DashboardResourceAssembler implements ResourceAssembler<Dashboard, Resource<Dashboard>> {

	@Override
	public Resource<Dashboard> toResource(Dashboard dashboard) {
		return new Resource<Dashboard>(dashboard);
	}
}
