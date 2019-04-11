package com.group17.feedback.filter;

import java.util.Date;

import com.group17.feedback.filter.impl.AgeFilter;
import com.group17.feedback.filter.impl.DashboardFilter;
import com.group17.feedback.filter.impl.RatingFilter;
import com.group17.feedback.filter.impl.SentimentFilter;
import com.group17.feedback.filter.impl.TextFilter;
import com.group17.feedback.tone.Sentiment;

public class FiltersBuilder {
	private Filters filters;
	
	private FiltersBuilder() {
		filters = new Filters();
	}
	
	public FiltersBuilder dashboard(String id) {
		filters.addFilter(new DashboardFilter(id));
		return this;
	}
	
	public FiltersBuilder age(Date sinceWhen) {
		filters.addFilter(new AgeFilter(sinceWhen));
		return this;
	}
	
	public FiltersBuilder age(long sinceWhen) {
		return age(new Date(sinceWhen));
	}
	
	public FiltersBuilder sentiment(Sentiment sentiment) {
		filters.addFilter(new SentimentFilter(sentiment));
		return this;
	}
	
	public FiltersBuilder rating(int rating) {
		filters.addFilter(new RatingFilter(rating));
		return this;
	}
	
	public FiltersBuilder text(String containing) {
		filters.addFilter(new TextFilter(containing));
		return this;
	}
	
	public Filters build() {
		return filters;
	}
	
	public static FiltersBuilder newInstance() {
		return new FiltersBuilder();
	}

}
