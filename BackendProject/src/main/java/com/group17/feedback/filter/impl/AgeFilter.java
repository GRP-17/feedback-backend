package com.group17.feedback.filter.impl;

import java.util.Date;

import com.group17.feedback.filter.Filter;
import com.group17.feedback.filter.FilterType;

public class AgeFilter extends Filter {
	private Date sinceWhen;
	
	public AgeFilter(Date sinceWhen) {
		super(FilterType.AGE);
		
		this.sinceWhen = sinceWhen;
	}
	
	public Date getSinceWhen() {
		return sinceWhen;
	}
	
}
