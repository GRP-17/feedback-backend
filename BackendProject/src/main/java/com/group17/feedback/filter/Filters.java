package com.group17.feedback.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
