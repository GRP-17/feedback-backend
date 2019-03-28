package com.group17.feedback.filter.query;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.logging.log4j.Level;

import com.group17.feedback.filter.FilterType;
import com.group17.feedback.filter.Filters;
import com.group17.feedback.filter.impl.AgeFilter;
import com.group17.util.LoggerUtil;

public class FeedbackBuilder extends QueryBuilder {
	private static final String BASE_QUERY = "SELECT f FROM Feedback f";
	private static final String ORDER_BY = " ORDER BY f.created DESC";

	public FeedbackBuilder(EntityManager entityManager, Filters filters) {
		super(entityManager, filters);
	}

	@Override
	public Query build() {
		String strQuery = BASE_QUERY
								.concat(buildWhereForFeedback())
								.concat(ORDER_BY);
		LoggerUtil.log(Level.INFO, "Query: " + strQuery);
		Query query = getEntityManager().createQuery(strQuery);
		
		if(getFilters().hasFilter(FilterType.AGE)) {
			AgeFilter ageFilter = (AgeFilter) getFilters().getFilter(FilterType.AGE);
			
			query = query.setParameter(AGE_PARAMETER_INDEX, 
									   ageFilter.getSinceWhen(), 
									   TemporalType.DATE);
		}
		
		return query;
	}
	
}
