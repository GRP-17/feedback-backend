package com.group17.feedback.filter.query;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.group17.feedback.filter.FilterType;
import com.group17.feedback.filter.Filters;
import com.group17.feedback.filter.impl.AgeFilter;

public class FeedbackIdsBuilder extends QueryBuilder {
	private static final String BASE_QUERY = "SELECT f.id FROM Feedback f";

	public FeedbackIdsBuilder(EntityManager entityManager, Filters filters) {
		super(entityManager, filters);
	}

	@Override
	public Query build() {
		String strQuery = BASE_QUERY.concat(buildWhereForFeedback());
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
