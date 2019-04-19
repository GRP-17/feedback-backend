package com.group17.feedback.filter.query;

import java.util.Map.Entry;

import com.group17.feedback.filter.Filter;
import com.group17.feedback.filter.FilterType;
import com.group17.feedback.filter.Filters;
import com.group17.feedback.filter.impl.LabelFilter;

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
			case LABEL:
				LabelFilter lf = (LabelFilter) entry.getValue();
				StringBuilder sb = new StringBuilder();
				
				for(int offset = 0; offset < lf.getLabelIds().size(); offset ++) {
					int index = PARAM_INDEX_LABEL + offset;
					if(offset > 0) {
						sb.append(" AND ");
					}
					sb.append("l.id.labelId=?" + index);
					offset ++;
				}
				buff.append(sb);
				break;
			}
			
			termCount ++;
		}
		
		return buff.toString();
	}
	
}
