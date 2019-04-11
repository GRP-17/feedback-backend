package com.group17.feedback.filter.impl;

import com.group17.feedback.filter.Filter;
import com.group17.feedback.filter.FilterType;
import com.group17.feedback.tone.Sentiment;

public class SentimentFilter extends Filter {
	private Sentiment sentiment;
	
	public SentimentFilter(Sentiment sentiment) {
		super(FilterType.SENTIMENT);
		
		this.sentiment = sentiment;
	}
	
	public Sentiment getSentiment() {
		return sentiment;
	}

}
