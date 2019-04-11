package com.group17.feedback.filter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.group17.feedback.filter.impl.AgeFilter;
import com.group17.feedback.filter.impl.DashboardFilter;
import com.group17.feedback.filter.impl.RatingFilter;
import com.group17.feedback.filter.impl.SentimentFilter;
import com.group17.feedback.filter.impl.TextFilter;
import com.group17.tone.Sentiment;
import com.group17.util.Constants;

public class Filters implements Cloneable {
	private Map<FilterType, Filter> filterMap;
	
	public Filters(Filter... filters) {
		filterMap = new HashMap<FilterType, Filter>();
		
		for(Filter filter : filters) {
			filterMap.put(filter.getType(), filter);
		}
	}
	
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
	
	@Override
	public Filters clone() {
		Filters filters = new Filters();
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
			}
		}
		return filters;
	}
	
	public static Filters fromParameters(String dashboardId, String query, 
										 long since, String sentiment) {

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
