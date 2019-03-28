package com.group17.feedback.filter.query;

import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.group17.feedback.filter.Filter;
import com.group17.feedback.filter.FilterType;
import com.group17.feedback.filter.Filters;
import com.group17.feedback.filter.impl.DashboardFilter;
import com.group17.feedback.filter.impl.RatingFilter;
import com.group17.feedback.filter.impl.SentimentFilter;
import com.group17.feedback.filter.impl.TextFilter;

public abstract class QueryBuilder {
	public static int AGE_PARAMETER_INDEX = 1;
	
	private final EntityManager entityManager;
	private final Filters filters;
	
	public QueryBuilder(EntityManager entityManager, Filters filters) {
		this.entityManager = entityManager;
		this.filters = filters;
	}

	public abstract Query build();
	
	public String buildWhereForFeedback() {
		StringBuffer buff = new StringBuffer();
		int termCount = 0;
		
		for(Entry<FilterType, Filter> entry : getFilters().entrySet()) {
			if(termCount == 0) {
				buff.append(" WHERE ");
			} else if(termCount > 0) {
				buff.append(" AND ");
			}
			
			switch(entry.getKey()) {
			case AGE:
				buff.append("f.created>?" + AGE_PARAMETER_INDEX);
				break;
			case DASHBOARD:
				DashboardFilter df = (DashboardFilter) entry.getValue();
				buff.append("f.dashboardId='" + df.getDashboardId() + "'");
				break;
			case SENTIMENT:
				SentimentFilter sf = (SentimentFilter) entry.getValue();
				buff.append("f.sentiment='" + sf.getSentiment().toString() + "'");
				break;
			case RATING:
				RatingFilter rf = (RatingFilter) entry.getValue();
				buff.append("f.rating=" + rf.getRating());
				break;
			case TEXT_CONTAINING:
				TextFilter tf = (TextFilter) entry.getValue();
				buff.append("f.text LIKE %" + tf.getText() + "%");
				break;
			}
			
			termCount ++;
		}
		
		return buff.toString();
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public Filters getFilters() {
		return filters;
	}

}
