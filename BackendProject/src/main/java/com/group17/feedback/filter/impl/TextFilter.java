package com.group17.feedback.filter.impl;

import com.group17.feedback.filter.Filter;
import com.group17.feedback.filter.FilterType;

public class TextFilter extends Filter {
	private String text;
	
	public TextFilter(String text) {
		super(FilterType.TEXT_CONTAINING);
		
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

}
