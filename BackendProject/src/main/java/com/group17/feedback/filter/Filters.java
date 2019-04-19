package com.group17.feedback.filter;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.lang.Nullable;

import com.group17.controller.FeedbackController;
import com.group17.dashboard.Dashboard;
import com.group17.feedback.filter.impl.AgeFilter;
import com.group17.feedback.filter.impl.DashboardFilter;
import com.group17.feedback.filter.impl.LabelFilter;
import com.group17.feedback.filter.impl.RatingFilter;
import com.group17.feedback.filter.impl.SentimentFilter;
import com.group17.feedback.filter.impl.TextFilter;
import com.group17.feedback.tone.Sentiment;
import com.group17.util.Constants;

public class Filters implements Cloneable {
	/** The {@link Filter}s this instance contains. */
	private Map<FilterType, Filter> filterMap;
	
	/**
	 * Constructor.
	 * 
	 * @param filters the pre-set filters
	 */
	protected Filters(Filter... filters) {
		filterMap = new HashMap<FilterType, Filter>();
		
		for(Filter filter : filters) {
			filterMap.put(filter.getType(), filter);
		}
	}
	
	/**
	 * Merge another Filters object into this instance.
	 * 
	 * @param toMerge the other Filters object to merge into this
	 * @param useNew if they both contain the same filter type, shall we overwrite?
	 * @return this (merged) Filters instance
	 */
	public Filters merge(Filters toMerge, boolean useNew) {
		Filters old = clone();
		
		for(Entry<FilterType, Filter> entry : toMerge.entrySet()) {
			FilterType type = entry.getKey();
			if(old.hasFilter(type) && !useNew)
				continue;

			old.filterMap.put(type, toMerge.getFilter(type));
		}
		return old;
	}
	
	/**
	 * Clone this Filters instance; creating all new copies of the variables
	 * inside.
	 */
	@Override
	public Filters clone() {
		Filters filters = new Filters();
		
		// Copy all of the filters inside to the new Map.
		// We don't want to reuse the same Map, as this won't
		// mean that we're cloning.
		for(Entry<FilterType, Filter> entry : filterMap.entrySet())
		{
			switch(entry.getKey()) {
			case AGE:
				AgeFilter af = (AgeFilter) entry.getValue();
				filters.addFilter(new AgeFilter(af.getSinceWhen()));
				break;
			case DASHBOARD:
				DashboardFilter df = (DashboardFilter) entry.getValue();
				filters.addFilter(new DashboardFilter(df.getDashboardId()));
				break;
			case RATING:
				RatingFilter rf = (RatingFilter) entry.getValue();
				filters.addFilter(new RatingFilter(rf.getRating()));
				break;
			case SENTIMENT:
				SentimentFilter sf = (SentimentFilter) entry.getValue();
				filters.addFilter(new SentimentFilter(sf.getSentiment()));
				break;
			case TEXT_CONTAINING:
				TextFilter tf = (TextFilter) entry.getValue();
				filters.addFilter(new TextFilter(tf.getText()));
				break;
			case LABEL:
				LabelFilter lf = (LabelFilter) entry.getValue();
				LinkedList<String> labelIds = new LinkedList<String>();
				for(String id : lf.getLabelIds()) {
					labelIds.add(id);
				}
				filters.addFilter(new LabelFilter(labelIds));
				break;
			}
		}
		return filters;
	}
	
	/**
	 * Get a {@link Filters} object from {@link FeedbackController} endpoint 
	 * parameters.
	 * 
	 * @param dashboardId the id of the {@link Dashboard} - required
	 * @param query the text filter to apply - nullable
	 * @param since the earliest date filter to apply - nullable
	 * @param sentiment the sentiment filter to apply - nullable
	 * @return the {@link Filters} object
	 */
	public static Filters fromParameters(String dashboardId,  
										 @Nullable String query, 
										 @Nullable long since,  
										 @Nullable String sentiment,
										 @Nullable List<String> labelId) {

		// dashboardId is always required
		Filters filters = new Filters(new DashboardFilter(dashboardId));
		
		if(query != null && !query.equals(Constants.PARAM_DEFAULT_STRING)) {
			filters.addFilter(new TextFilter(query));
		}
		if(since != Constants.PARAM_DEFAULT_LONG_VALUE) {
			filters.addFilter(new AgeFilter(new Date(since)));
		}
		if(sentiment != null && !sentiment.equals(Constants.PARAM_DEFAULT_STRING)) {
			Sentiment sentimentEnum = Sentiment.valueOf(sentiment);
			filters.addFilter(new SentimentFilter(sentimentEnum));
		}
		if(labelId != null) {
			filters.addFilter(new LabelFilter(labelId));
		}
		
		return filters;
	}
	
	public boolean hasFilter(FilterType type) {
		return filterMap.containsKey(type);
	}
	
	public Filter getFilter(FilterType type) {
		return filterMap.get(type);
	}
	
	public void addFilter(Filter filter) {
		filterMap.put(filter.getType(), filter);
	}
	
	public Set<Entry<FilterType, Filter>> entrySet() {
		return filterMap.entrySet();
	}
	
}
