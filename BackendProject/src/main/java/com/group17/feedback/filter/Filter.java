package com.group17.feedback.filter;

/**
 * A filter that can be applied to JPA repository queries.
 */
public abstract class Filter {
	private FilterType type;
	
	/**
	 * Constructor.
	 * 
	 * @param type the type of Filter
	 */
	public Filter(FilterType type) {
		this.type = type;
	}
	
	public FilterType getType() {
		return type;
	}
	
}
