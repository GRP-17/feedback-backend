package com.group17.feedback.filter.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.group17.feedback.filter.Filter;
import com.group17.feedback.filter.FilterType;

public class LabelFilter extends Filter {
	private List<String> labelIds;
	
	public LabelFilter(Collection<String> labels) {
		super(FilterType.LABEL);
		
		this.labelIds = new LinkedList<String>(labels);
	}
	
	public LabelFilter(String... labels) {
		this(Arrays.asList(labels));
	}
	
	public List<String> getLabelIds() {
		return labelIds;
	}
 
}
