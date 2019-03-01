package com.group17.feedback.NegativePerDay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface NegativePerDayRepository extends JpaRepository<NegativePerDay, Date> {
	List<NegativePerDay> findAllByOrderByDateAsc();
}
