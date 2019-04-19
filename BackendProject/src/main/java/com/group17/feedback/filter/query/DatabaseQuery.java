package com.group17.feedback.filter.query;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.group17.feedback.filter.Filter;
import com.group17.feedback.filter.FilterType;
import com.group17.feedback.filter.Filters;
import com.group17.feedback.filter.impl.AgeFilter;
import com.group17.feedback.filter.impl.DashboardFilter;
import com.group17.feedback.filter.impl.LabelFilter;
import com.group17.feedback.filter.impl.RatingFilter;
import com.group17.feedback.filter.impl.SentimentFilter;
import com.group17.feedback.filter.impl.TextFilter;

public abstract class DatabaseQuery {
	/** The query parameter for {@link FilterType#DASHBOARD}. */
	public static int PARAM_INDEX_DASHBOARD = 1;
	/** The query parameter for {@link FilterType#AGE} */
	public static int PARAM_INDEX_AGE 		= 2;
	/** The query parameter for {@link FilterType#TEXT_CONTAINING}. */
	public static int PARAM_INDEX_TEXT 		= 3;
	/** The query parameter for {@link FilterType#SENTIMENT}. */
	public static int PARAM_INDEX_SENTIMENT = 4;
	/** The query parameter for {@link FilterType#RATING}. */
	public static int PARAM_INDEX_RATING    = 5;
	/** The query parameter for {@link FilterType#LABEL}. 
	 *  <p>
	 *  
	 */
	public static int PARAM_INDEX_LABEL     = 100;

	/**
	 * Build a {@link javax.persistence.Query}, for this DatabaseQuery
	 * based on Filters.
	 * 
	 * @param entityManager the {@link javax.persistence.EntityManager} to apply
	 * @param filters the {@link com.group17.feedback.filter.Filters} to apply
	 * @return the query to be executed
	 */
	public abstract Query build(EntityManager entityManager, Filters filters);
	
	public abstract String buildWhere(Filters filters, List<String> whereClauses);
	
	public String buildWhere(Filters filters, String... whereClauses) {
		return buildWhere(filters, Arrays.asList(whereClauses));
	}
	
	/**
	 * Set the {@link javax.persistence.Query} parameters based on filters.
	 * 
	 * @param query the {@link javax.persistence.Query} to apply it to
	 * @param filters the filters to add parameters for
	 * @return the same, modified {@link javax.persistence.Query} instance
	 */
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
			case LABEL:
				LabelFilter lf = (LabelFilter) entry.getValue();
				int offset = 0;
				for(String labelId : lf.getLabelIds()) {
					int index = PARAM_INDEX_LABEL + offset++;
					query = query.setParameter(index, labelId);
				}
				break;
			}
		}
		return query;
	}
	
}
