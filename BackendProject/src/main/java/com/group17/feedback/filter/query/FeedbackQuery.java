package com.group17.feedback.filter.query;

import java.util.List;
import java.util.Map.Entry;

import com.group17.feedback.filter.Filter;
import com.group17.feedback.filter.FilterType;
import com.group17.feedback.filter.Filters;
import com.group17.feedback.filter.impl.LabelFilter;

public abstract class FeedbackQuery extends DatabaseQuery {

	@Override
	public String buildWhere(Filters filters, 
							 List<String> whereClauses) {
		
		for(Entry<FilterType, Filter> entry : filters.entrySet()) {
			switch(entry.getKey()) {
			case DASHBOARD:
				whereClauses.add("f.dashboardId=?" + PARAM_INDEX_DASHBOARD);
				break;
			case AGE:
				whereClauses.add("f.created>?" + PARAM_INDEX_AGE);
				break;
			case TEXT_CONTAINING:
				whereClauses.add("f.text LIKE ?" + PARAM_INDEX_TEXT);
				break;
			case SENTIMENT:
				whereClauses.add("f.sentiment=?" + PARAM_INDEX_SENTIMENT);
				break;
			case RATING:
				whereClauses.add("f.rating=?" + PARAM_INDEX_RATING);
				break;
			case LABEL:
				LabelFilter lf = (LabelFilter) entry.getValue();
				for(int offset = 0; offset < lf.getLabelIds().size(); offset ++) {
					int index = PARAM_INDEX_LABEL + offset;
					whereClauses.add("l.id.labelId=?" + index);
				}
				break;
			}
		}
		
		StringBuffer buff = new StringBuffer();
		int termCount = 0;
		for(String clause : whereClauses) {
			if(termCount == 0)
				buff.append(" WHERE ");
			else if(termCount > 0)
				buff.append(" AND ");
			
			buff.append(clause);
			termCount ++;
		}
		
		return buff.toString();
	}
	
	public String buildOrderByPinned() {
		return " ORDER BY f.pinned DESC";
	}
	
}
