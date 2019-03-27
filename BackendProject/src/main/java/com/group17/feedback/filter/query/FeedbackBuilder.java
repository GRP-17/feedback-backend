package com.group17.feedback.filter.query;

import javax.persistence.EntityManager;

public class FeedbackBuilder extends QueryBuilder {

	public FeedbackBuilder(EntityManager entityManager, int dashboardId) {
		super(entityManager);
		
		addWhereClause("dashboardId", String.valueOf(dashboardId));
	}
	
}
