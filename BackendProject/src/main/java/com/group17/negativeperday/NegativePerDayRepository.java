package com.group17.negativeperday;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NegativePerDayRepository extends JpaRepository<NegativePerDay, Date> {
	
	@Query("SELECT n FROM NegativePerDay n WHERE n.date=?1")
	public NegativePerDay getByDate(Date date);
	
}
