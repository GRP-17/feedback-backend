package com.group17.feedback.filter.query;

import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.logging.log4j.Level;

import com.group17.feedback.filter.Filter;
import com.group17.feedback.filter.FilterType;
import com.group17.feedback.filter.Filters;
import com.group17.feedback.filter.impl.AgeFilter;
import com.group17.feedback.filter.impl.DashboardFilter;
import com.group17.feedback.filter.impl.RatingFilter;
import com.group17.feedback.filter.impl.SentimentFilter;
import com.group17.feedback.filter.impl.TextFilter;
import com.group17.util.LoggerUtil;

public class NegativePerDayBuilder extends QueryBuilder {
	private static final String BASE_QUERY = "SELECT DISTINCT(n) FROM Feedback f, NegativePerDay n";

	public NegativePerDayBuilder(EntityManager entityManager, Filters filters) {
		super(entityManager, filters);
	}

	@Override
	public Query build() {
		String strQuery = BASE_QUERY.concat(buildWhereForNegativePerDay());
		
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
	
	private String buildWhereForNegativePerDay() {
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
				buff.append("n.date > ?" + AGE_PARAMETER_INDEX);
				break;
			case DASHBOARD:
				DashboardFilter df = (DashboardFilter) entry.getValue();
				buff.append("n.dashboardId='" + df.getDashboardId() + "'");
				break;
			case SENTIMENT:
				SentimentFilter sf = (SentimentFilter) entry.getValue();
				buff.append("f.sentiment='" + sf.getSentiment() + "'");
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

}
