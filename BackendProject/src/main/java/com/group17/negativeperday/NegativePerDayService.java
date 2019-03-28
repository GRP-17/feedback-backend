package com.group17.negativeperday;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Service;

import com.group17.feedback.filter.Filters;
import com.group17.feedback.filter.FiltersBuilder;
import com.group17.feedback.filter.query.NegativePerDayBuilder;
import com.group17.feedback.filter.query.QueryBuilder;
import com.group17.util.DateUtil;

@Service
public class NegativePerDayService {
	@Autowired private JpaContext jpaContext;
	@Autowired private NegativePerDayRepository negativePerDayRepository;

	public void increaseNegativeByDate(Date date, int increment) {
		// ensure this is midnight date
		date = DateUtil.getDayStart(date);

		if (negativePerDayRepository.existsById(date)) {
			// update
			NegativePerDay existedNegativePerDay = negativePerDayRepository.getOne(date);

			existedNegativePerDay.increaseVolume(increment);
			negativePerDayRepository.save(existedNegativePerDay);
		} else {
			// create
			NegativePerDay negativePerDay = new NegativePerDay(date, 1);
			negativePerDayRepository.save(negativePerDay);
		}
	}

	public void increaseNegativeByDate(Date date) {
		increaseNegativeByDate(date, 1);
	}

	public HashMap<String, Object> findNegativePerDay(String dashboardId) {
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		
		Filters filters = FiltersBuilder
								.newInstance()
								.dashboard(dashboardId)
								.build();
		// TODO: Only return limited dates
		
		QueryBuilder builder = new NegativePerDayBuilder(getNPDEntityManager(), 
														 filters);
		for(Object object : builder.build().getResultList()) {
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
