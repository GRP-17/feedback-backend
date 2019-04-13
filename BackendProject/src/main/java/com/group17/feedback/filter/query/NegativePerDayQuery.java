package com.group17.feedback.filter.query;

import java.util.Map.Entry;

import com.group17.feedback.filter.Filter;
import com.group17.feedback.filter.FilterType;
import com.group17.feedback.filter.Filters;

public abstract class NegativePerDayQuery extends DatabaseQuery {

	public String buildWhere(Filters filters) {
		StringBuffer buff = new StringBuffer();
		int termCount = 0;
		
		for(Entry<FilterType, Filter> entry : filters.entrySet()) {
			if(termCount == 0) {
				buff.append(" WHERE ");
			} else if(termCount > 0) {
				buff.append(" AND ");
			}
			
			switch(entry.getKey()) {
			case DASHBOARD:
				buff.append("n.dashboardId=?" + PARAM_INDEX_DASHBOARD);
				break;
			case AGE:
				buff.append("n.date>?" + PARAM_INDEX_AGE);
				break;
			case TEXT_CONTAINING:
				buff.append("f.text LIKE ?" + PARAM_INDEX_TEXT);
				break;
			case SENTIMENT:
				buff.append("f.sentiment=?" + PARAM_INDEX_SENTIMENT);
				break;
			case RATING:
				buff.append("f.rating=?" + PARAM_INDEX_RATING);
				break;
			}
			
			termCount ++;
		}
		
		return buff.toString();
	}
	
}
