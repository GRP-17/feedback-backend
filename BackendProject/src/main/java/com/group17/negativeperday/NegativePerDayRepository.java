package com.group17.negativeperday;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NegativePerDayRepository extends JpaRepository<NegativePerDay, Date> {
	
	@Query("SELECT n FROM NegativePerDay n WHERE n.dashboardId=?1 AND n.date=?2")
	public NegativePerDay getEntity(String dashboardId, Date date);
	
}
