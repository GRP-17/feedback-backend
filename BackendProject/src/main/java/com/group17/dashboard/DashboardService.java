package com.group17.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.group17.exception.CommonException;

@Service
public class DashboardService {
	@Autowired private DashboardRepository repository;
	
	public Dashboard getDashboardById(String dashboardId) {
		Dashboard dashboard = repository.findById(dashboardId)
				.orElseThrow(() -> new CommonException("Could not find dashboard: " + dashboardId, 
													   HttpStatus.NOT_FOUND.value()));
		return dashboard;
	}

}
