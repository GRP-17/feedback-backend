package com.group17.feedback.filter.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.Level;

import com.group17.feedback.filter.FilterType;
import com.group17.feedback.filter.Filters;
import com.group17.util.LoggerUtil;

public class Queries {
	public static final DatabaseQuery FEEDBACK = new FeedbackQuery() {
		final String BASE_QUERY = "SELECT DISTINCT(f) "
										+ "FROM Feedback f";
		final String ORDER_BY = " ORDER BY f.pinned DESC, f.created DESC";

		@Override
		public Query build(EntityManager entityManager, Filters filters) {
			List<String> extraWhereClauses = new ArrayList<String>();
			
			String strQuery = BASE_QUERY;
			if(filters.hasFilter(FilterType.LABEL)) {
				// The resultant set would be empty if the FeedbackLabel
				// table is empty otherwise. Proof: a x 0 = 0
				strQuery = strQuery.concat(", FeedbackLabel l");
				extraWhereClauses.add("l.id.feedbackId=f.feedbackId");
			}
			strQuery = strQuery.concat(buildWhere(filters, extraWhereClauses))
							   .concat(ORDER_BY);
			
			LoggerUtil.log(Level.INFO, "Prepared FEEDBACK query: " + strQuery);
			return setParameters(entityManager.createQuery(strQuery), filters);
		}
		
	};
	public static final DatabaseQuery FEEDBACK_IDS = new FeedbackQuery() {
		final String BASE_QUERY = "SELECT DISTINCT(f.id) "
										+ "FROM Feedback f";

		@Override
		public Query build(EntityManager entityManager, Filters filters) {
			List<String> extraWhereClauses = new ArrayList<String>();
			
			String strQuery = BASE_QUERY;
			if(filters.hasFilter(FilterType.LABEL)) {
				// The resultant set would be empty if the FeedbackLabel
				// table is empty otherwise. Proof: a x 0 = 0
				strQuery = strQuery.concat(", FeedbackLabel l");
				extraWhereClauses.add("l.id.feedbackId=f.feedbackId");
			}
			strQuery = strQuery.concat(buildWhere(filters, extraWhereClauses))
							   .concat(buildOrderByPinned());
			
			LoggerUtil.log(Level.INFO, "Prepared FEEDBACK_IDS query: " + strQuery);
			return setParameters(entityManager.createQuery(strQuery), filters);
		}
		
	};
	public static final DatabaseQuery COUNT = new FeedbackQuery() {
		final String BASE_QUERY = "SELECT COUNT(DISTINCT f) "
										+ "FROM Feedback f";

		@Override
		public Query build(EntityManager entityManager, Filters filters) {
			List<String> extraWhereClauses = new ArrayList<String>();
			
			String strQuery = BASE_QUERY;
			if(filters.hasFilter(FilterType.LABEL)) {
				// The resultant set would be empty if the FeedbackLabel
				// table is empty otherwise. Proof: a x 0 = 0
				strQuery = strQuery.concat(", FeedbackLabel l");
				extraWhereClauses.add("l.id.feedbackId=f.feedbackId");
			}
			strQuery = strQuery.concat(buildWhere(filters, extraWhereClauses))
							   .concat(buildOrderByPinned());
			LoggerUtil.log(Level.INFO, "Prepared COUNT query: " + strQuery);
			return setParameters(entityManager.createQuery(strQuery), filters);
		}
		
	};
	
	public static final DatabaseQuery RATING_COUNT = new FeedbackQuery() {
		final String BASE_QUERY = "SELECT COUNT(f.rating) FROM Feedback f WHERE f IN";
		final String SUB_QUERY = "SELECT DISTINCT(f) FROM Feedback f";

		@Override
		public Query build(EntityManager entityManager, Filters filters) {
			List<String> extraWhereClauses = new ArrayList<String>();
			
			String strQuery = BASE_QUERY
							   	.concat("(")
							   	.concat(SUB_QUERY);
			if(filters.hasFilter(FilterType.LABEL)) {
				// The resultant set would be empty if the FeedbackLabel
				// table is empty otherwise. Proof: a x 0 = 0
				strQuery = strQuery.concat(", FeedbackLabel l");
				extraWhereClauses.add("l.id.feedbackId=f.feedbackId");
			}
			strQuery = strQuery.concat(buildWhere(filters, extraWhereClauses))
							   .concat(buildOrderByPinned())
							   .concat(")");
			
			LoggerUtil.log(Level.INFO, "Prepared RATING_COUNT query: " + strQuery);
			return setParameters(entityManager.createQuery(strQuery), filters);
		}
		
	};
	public static final DatabaseQuery SENTIMENT_COUNT = new FeedbackQuery() {
		final String BASE_QUERY = "SELECT COUNT(f.sentiment) FROM Feedback f WHERE f IN";
		final String SUB_QUERY = "SELECT DISTINCT(f) FROM Feedback f";

		@Override
		public Query build(EntityManager entityManager, Filters filters) {
			List<String> extraWhereClauses = new ArrayList<String>();
			
			String strQuery = BASE_QUERY
								.concat("(")
								.concat(SUB_QUERY);
			if(filters.hasFilter(FilterType.LABEL)) {
				// The resultant set would be empty if the FeedbackLabel
				// table is empty otherwise. Proof: a x 0 = 0
				strQuery = strQuery.concat(", FeedbackLabel l");
				extraWhereClauses.add("l.id.feedbackId=f.feedbackId");
			}
			strQuery = strQuery.concat(buildWhere(filters, extraWhereClauses))
							   .concat(buildOrderByPinned())
							    .concat(")");
			
			LoggerUtil.log(Level.INFO, "Prepared SENTIMENT_COUNT query: " + strQuery);
			return setParameters(entityManager.createQuery(strQuery), filters);
		}
		
	};
	public static final DatabaseQuery NEGATIVE_PER_DAY = new NegativePerDayQuery() {
		final String BASE_QUERY = "SELECT DISTINCT(n) "
										+ "FROM Feedback f, NegativePerDay n";
		final String ORDER_BY = " ORDER BY f.pinned DESC";

		@Override
		public Query build(EntityManager entityManager, Filters filters) {
			List<String> extraWhereClauses = new ArrayList<String>();
			
			String strQuery = BASE_QUERY;
			if(filters.hasFilter(FilterType.LABEL)) {
				// The resultant set would be empty if the FeedbackLabel
				// table is empty otherwise. Proof: a x 0 = 0
				strQuery = strQuery.concat(", FeedbackLabel l");
				extraWhereClauses.add("l.id.feedbackId=f.feedbackId");
			}
			strQuery = strQuery.concat(buildWhere(filters, extraWhereClauses))
							   .concat(ORDER_BY);
			
			LoggerUtil.log(Level.INFO, "Prepared NEGATIVE_PER_DAY query: " + strQuery);
			return setParameters(entityManager.createQuery(strQuery), filters);
		}
		
	};
	
	private Queries() {}

}
