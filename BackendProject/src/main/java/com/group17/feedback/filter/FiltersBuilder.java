package com.group17.feedback.filter;

import java.util.Date;

import com.group17.feedback.filter.impl.AgeFilter;
import com.group17.feedback.filter.impl.DashboardFilter;
import com.group17.feedback.filter.impl.RatingFilter;
import com.group17.feedback.filter.impl.SentimentFilter;
import com.group17.feedback.filter.impl.TextFilter;
import com.group17.feedback.tone.Sentiment;

/**
 * A builder class for creating {@link Filters}.
 */
public class FiltersBuilder {
	/** The {@link Filters} object we're building. */
	private Filters filters;
	
	/**
	 * Constructor.
	 */
	private FiltersBuilder() {
		filters = new Filters();
	}
	
	/**
	 * Create a new FiltersBuilder instance.
	 * 
	 * @return the builder
	 */
	public static FiltersBuilder newInstance() {
		return new FiltersBuilder();
	}
	
	/**
	 * Add a {@link FilterType#DASHBOARD} filter.
	 * 
	 * @param id the dashboardId
	 * @return the same builder instance
	 */
	public FiltersBuilder dashboard(String id) {
		filters.addFilter(new DashboardFilter(id));
		return this;
	}
	
	/**
	 * Add an {@link FilterType#AGE} filter.
	 * 
	 * @param sinceWhen the earliest date
	 * @return the same builder instance
	 */
	public FiltersBuilder age(Date sinceWhen) {
		filters.addFilter(new AgeFilter(sinceWhen));
		return this;
	}
	
	/**
	 * Add an {@link FilterType#AGE} filter.
	 * 
	 * @param sinceWhen the earliest date
	 * @return the same builder instance
	 */
	public FiltersBuilder age(long sinceWhen) {
		return age(new Date(sinceWhen));
	}
	
	/**
	 * Add a {@link FilterType#SENTIMENT} filter.
	 * 
	 * @param sentiment the sentiment
	 * @return the same builder instance
	 */
	public FiltersBuilder sentiment(Sentiment sentiment) {
		filters.addFilter(new SentimentFilter(sentiment));
		return this;
	}
	
	/**
	 * Add a {@link FilterType#RATING} filter.
	 * 
	 * @param rating the rating
	 * @return the same builder instance
	 */
	public FiltersBuilder rating(int rating) {
		filters.addFilter(new RatingFilter(rating));
		return this;
	}
	
	/**
	 * Add a {@link FilterType#TEXT_CONTAINING} filter.
	 * 
	 * @param containing the text filter
	 * @return the same builder instance
	 */
	public FiltersBuilder text(String containing) {
		filters.addFilter(new TextFilter(containing));
		return this;
	}
	
	/**
	 * Build the {@link Filters} object.
	 * 
	 * @return the created object
	 */
	public Filters build() {
		return filters;
	}

}
