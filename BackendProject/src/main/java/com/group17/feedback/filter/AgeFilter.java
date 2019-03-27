package com.group17.feedback.filter;

import java.util.Date;

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
