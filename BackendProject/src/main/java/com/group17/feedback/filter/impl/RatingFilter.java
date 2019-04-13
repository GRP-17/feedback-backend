package com.group17.feedback.filter.impl;

import com.group17.feedback.filter.Filter;
import com.group17.feedback.filter.FilterType;

public class RatingFilter extends Filter {
	private int rating;
	
	public RatingFilter(int rating) {
		super(FilterType.RATING);
		
		this.rating = rating;
	}
	
	public int getRating() {
		return rating;
	}

}
