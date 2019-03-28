package com.group17.feedback.filter;

public abstract class Filter {
	private FilterType type;
	
	public Filter(FilterType type) {
		this.type = type;
	}
	
	public FilterType getType() {
		return type;
	}
	
}
