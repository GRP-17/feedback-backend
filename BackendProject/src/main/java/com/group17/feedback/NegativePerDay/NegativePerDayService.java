package com.group17.feedback.NegativePerDay;

import com.group17.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NegativePerDayService {
	@Autowired
	private NegativePerDayRepository negativePerDayRepository;

	public void increaseNegativeByDate(int increment, Date date) {
		// ensure this is midnight date
		date = DateUtil.getDayStart(date);
		System.out.println(date.toString());

		if (negativePerDayRepository.existsById(date)) {
			// update
			NegativePerDay existedNegativePerDay = negativePerDayRepository.getOne(date);

			System.out.println(existedNegativePerDay.getDate());
			existedNegativePerDay.increaseVolume(increment);
			System.out.println(existedNegativePerDay.getDate());
			NegativePerDay entity = negativePerDayRepository.save(existedNegativePerDay);
			System.out.println(entity.getDate());
		} else {
			// create
			NegativePerDay negativePerDay = new NegativePerDay(date, 1);
			negativePerDayRepository.save(negativePerDay);
		}
	}

	public void increaseNegativeByDate(Date date) {
		increaseNegativeByDate(1, date);
	}

	public HashMap<String, Object> findNegativePerDay() {
		// TODO: return only limited dates
		List<NegativePerDay> negativePerDays = negativePerDayRepository.findAllByOrderByDateAsc();


		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		for (NegativePerDay negativePerDay : negativePerDays) {

			maps.add(new HashMap<String, Object>() {{
				put("date", negativePerDay.getDate());
				put("volume", negativePerDay.getVolume());
			}});
		}

		return new HashMap<String, Object>() {{
			put("result", maps);
		}};
	}
}
