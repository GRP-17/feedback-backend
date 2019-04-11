package com.group17.feedback.filter.query;

import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.Level;

import com.group17.feedback.filter.Filter;
import com.group17.feedback.filter.FilterType;
import com.group17.feedback.filter.Filters;
import com.group17.util.LoggerUtil;

public class Queries {
	public static final DatabaseQuery FEEDBACK = new FeedbackQuery() {
		final String BASE_QUERY = "SELECT f FROM Feedback f";
		final String ORDER_BY = " ORDER BY f.created DESC";

		@Override
		public Query build(EntityManager entityManager, Filters filters) {
			String strQuery = BASE_QUERY
									.concat(buildWhere(filters))
									.concat(ORDER_BY);
			LoggerUtil.log(Level.INFO, "Prepared FEEDBACK query: " + strQuery);
			return setParameters(entityManager.createQuery(strQuery), filters);
		}
		
	};
	public static final DatabaseQuery FEEDBACK_IDS = new FeedbackQuery() {
		final String BASE_QUERY = "SELECT f.id FROM Feedback f";

		@Override
		public Query build(EntityManager entityManager, Filters filters) {
			String strQuery = BASE_QUERY .concat(buildWhere(filters));
			LoggerUtil.log(Level.INFO, "Prepared FEEDBACK_IDS query: " + strQuery);
			return setParameters(entityManager.createQuery(strQuery), filters);
		}
		
	};
	public static final DatabaseQuery COUNT = new FeedbackQuery() {
		final String BASE_QUERY = "SELECT COUNT(f) FROM Feedback f";

		@Override
		public Query build(EntityManager entityManager, Filters filters) {
			String strQuery = BASE_QUERY.concat(buildWhere(filters));
			LoggerUtil.log(Level.INFO, "Prepared COUNT query: " + strQuery);
			return setParameters(entityManager.createQuery(strQuery), filters);
		}
		
	};
	public static final DatabaseQuery RATING_COUNT = new FeedbackQuery() {
		final String BASE_QUERY = "SELECT COUNT(f.rating) FROM Feedback f";

		@Override
		public Query build(EntityManager entityManager, Filters filters) {
			String strQuery = BASE_QUERY.concat(buildWhere(filters));
			LoggerUtil.log(Level.INFO, "Prepared RATING_COUNT query: " + strQuery);
			return setParameters(entityManager.createQuery(strQuery), filters);
		}
		
	};
	public static final DatabaseQuery SENTIMENT_COUNT = new FeedbackQuery() {
		final String BASE_QUERY = "SELECT COUNT(f.sentiment) FROM Feedback f";

		@Override
		public Query build(EntityManager entityManager, Filters filters) {
			String strQuery = BASE_QUERY.concat(buildWhere(filters));
			LoggerUtil.log(Level.INFO, "Prepared SENTIMENT_COUNT query: " + strQuery);
			return setParameters(entityManager.createQuery(strQuery), filters);
		}
		
	};
	public static final DatabaseQuery NEGATIVE_PER_DAY = new DatabaseQuery() {
		final String BASE_QUERY = "SELECT DISTINCT(n) FROM Feedback f, NegativePerDay n";

		@Override
		public Query build(EntityManager entityManager, Filters filters) {
			String strQuery = BASE_QUERY.concat(buildWhere(filters));
			LoggerUtil.log(Level.INFO, "Prepared NEGATIVE_PER_DAY query: " + strQuery);
			return setParameters(entityManager.createQuery(strQuery), filters);
		}
		
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
		
	};
	
	private Queries() {}

}
