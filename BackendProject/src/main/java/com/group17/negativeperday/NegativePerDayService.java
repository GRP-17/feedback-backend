package com.group17.negativeperday;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Service;

import com.group17.feedback.filter.Filters;
import com.group17.feedback.filter.FiltersBuilder;
import com.group17.feedback.filter.query.Queries;
import com.group17.util.DateUtil;

/**
 * The {@link org.springframework.stereotype.Service} that will handle any retrieval
 * of {@link NegativePerDay} related data.
 */
@Service
public class NegativePerDayService {
	/** Holds the JpaContext instance for entity managers. */
	@Autowired private JpaContext jpaContext;
	/** Holds the instance of the NegativePerDayRepository which represents the database. */
	@Autowired private NegativePerDayRepository negativePerDayRepository;

	/**
	 * Increase the {@link NegativePerDay} counter for a given day.
	 * <p>
	 * A {@link NegativePerDay} will be created if one for this date doesn't exist.
	 * 
	 * @param dashboardId the id of the {@link Dashboard} to increase the tracker of
	 * @param date which day to increase it on
	 * @param amount the amount to increase it by
	 */
	public void increaseNegativeByDate(String dashboardId, Date date, int amount) {
		Filters filters = FiltersBuilder.newInstance().dashboard(dashboardId).build();
		
		NegativePerDay byDate = null;
		for(Object obj : Queries.NEGATIVE_PER_DAY.build(getNPDEntityManager(), filters)
							.getResultList()) {
			NegativePerDay day = (NegativePerDay) obj;
			if(day.getDashboardId().equals(dashboardId) 
					&& DateUtil.getDayStart(day.getDate()).equals(DateUtil.getDayStart(date))) {
				byDate = day;
			}
		}
		
		if (byDate != null) {
			// NegativePerDay already exists - let's just update
			byDate.increaseVolume(amount);
			negativePerDayRepository.save(byDate);
		} else {
			// NegativePerDay doesn't exist - let's create
			NegativePerDay negativePerDay = new NegativePerDay(dashboardId, date, 1);
			negativePerDayRepository.save(negativePerDay);
		}
	}

	/**
	 * Increase the {@link NegativePerDay} counter for a given day by 1.
	 * <p>
	 * A {@link NegativePerDay} will be created if one for this date doesn't exist.
	 * 
	 * @param dashboardId the id of the {@link Dashboard} to increase the tracker of
	 * @param date which day to increase it on
	 */
	public void incrementNegativeByDate(String dashboardId, Date date) {
		increaseNegativeByDate(dashboardId, date, 1);
	}

	/**
	 * Get a {@link java.util.Map} containing {@link java.util.Map}s; which hold
	 * the dates & volumes of recent {@link NegativePerDays}.
	 * <p>
	 * If no entries are found, the {@link java.util.Map} will empty.
	 * 
	 * @param filters the filters to apply 
	 * @return the resultant {@link java.util.Map}
	 */
	public HashMap<String, Object> findNegativePerDay(Filters filters) {
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		
		Query query = Queries.NEGATIVE_PER_DAY.build(getNPDEntityManager(), filters);
		for(Object object : query.getResultList()) {
			NegativePerDay npd = (NegativePerDay) object;
			maps.add(new HashMap<String, Object>() {{
				put("date", npd.getDate());
				put("volume", npd.getVolume());
			}});
		}
		
		return new HashMap<String, Object>() {{
			put("result", maps);
		}};
	}
	
	/**
	 * Get the {@link javax.persistence.EntityManager} for the {@link NegativePerDay} entity.
	 * 
	 * @return the {@link javax.persistence.EntityManager}
	 */
	private EntityManager getNPDEntityManager() {
		return jpaContext.getEntityManagerByManagedType(NegativePerDay.class);
	}
	
}
