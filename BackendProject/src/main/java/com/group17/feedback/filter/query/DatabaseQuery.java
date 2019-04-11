package com.group17.feedback.filter.query;

import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.group17.feedback.filter.Filter;
import com.group17.feedback.filter.FilterType;
import com.group17.feedback.filter.Filters;
import com.group17.feedback.filter.impl.AgeFilter;
import com.group17.feedback.filter.impl.DashboardFilter;
import com.group17.feedback.filter.impl.RatingFilter;
import com.group17.feedback.filter.impl.SentimentFilter;
import com.group17.feedback.filter.impl.TextFilter;

public abstract class DatabaseQuery {
	public static int PARAM_INDEX_DASHBOARD = 1;
	public static int PARAM_INDEX_AGE 		= 2;
	public static int PARAM_INDEX_TEXT 		= 3;
	public static int PARAM_INDEX_SENTIMENT = 4;
	public static int PARAM_INDEX_RATING    = 5;

	public abstract Query build(EntityManager entityManager, Filters filters);
	
	public Query setParameters(Query query, Filters filters) {
		for(Entry<FilterType, Filter> entry : filters.entrySet()) {
			switch(entry.getKey()) {
			case DASHBOARD:
				DashboardFilter df = (DashboardFilter) entry.getValue();
				query = query.setParameter(PARAM_INDEX_DASHBOARD, 
										   df.getDashboardId());
				break;
			case AGE:
				AgeFilter af = (AgeFilter) entry.getValue();
				query = query.setParameter(PARAM_INDEX_AGE, 
										   af.getSinceWhen(), 
										   TemporalType.DATE);
				break;
			case TEXT_CONTAINING:
				TextFilter tf = (TextFilter) entry.getValue();
				query = query.setParameter(PARAM_INDEX_TEXT, 
										   "%" + tf.getText() + "%");
				break;
			case SENTIMENT:
				SentimentFilter sf = (SentimentFilter) entry.getValue();
				query = query.setParameter(PARAM_INDEX_SENTIMENT,
										   sf.getSentiment().toString());
				break;
			case RATING:
				RatingFilter rf = (RatingFilter) entry.getValue();
				query = query.setParameter(PARAM_INDEX_RATING, 
										   rf.getRating());
				break;
			}
		}
		return query;
	}
	
	
}
