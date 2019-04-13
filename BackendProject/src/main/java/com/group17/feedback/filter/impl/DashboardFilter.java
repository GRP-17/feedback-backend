package com.group17.feedback.filter.impl;

import com.group17.feedback.filter.Filter;
import com.group17.feedback.filter.FilterType;

public class DashboardFilter extends Filter {
	private String dashboardId;
	
	public DashboardFilter(String dashboardId) {
		super(FilterType.DASHBOARD);
		
		this.dashboardId = dashboardId;
	}
	
	public String getDashboardId() {
		return dashboardId;
	}

}
