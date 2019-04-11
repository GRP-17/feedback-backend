package com.group17.feedback.filter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.group17.feedback.filter.impl.AgeFilter;
import com.group17.feedback.filter.impl.DashboardFilter;
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
	
	@Override
	public Filters clone() {
		try {
			return (Filters) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
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
