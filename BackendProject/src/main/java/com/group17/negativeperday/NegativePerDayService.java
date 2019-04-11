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
import com.group17.feedback.filter.query.Queries;
import com.group17.util.DateUtil;

@Service
public class NegativePerDayService {
	@Autowired private JpaContext jpaContext;
	@Autowired private NegativePerDayRepository negativePerDayRepository;

	public void increaseNegativeByDate(String dashboardId, Date date, int increment) {
		// ensure this is midnight date
		date = DateUtil.getDayStart(date);

		NegativePerDay byDate = negativePerDayRepository.getByDate(date);
		if (byDate != null) {
			// update
			byDate.increaseVolume(increment);
			negativePerDayRepository.save(byDate);
		} else {
			// create
			NegativePerDay negativePerDay = new NegativePerDay(dashboardId, date, 1);
			negativePerDayRepository.save(negativePerDay);
		}
	}

	public void increaseNegativeByDate(String dashboardId, Date date) {
		increaseNegativeByDate(dashboardId, date, 1);
	}

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
	
	private EntityManager getNPDEntityManager() {
		return jpaContext.getEntityManagerByManagedType(NegativePerDay.class);
	}
	
}
