package com.group17.dashboard;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.group17.exception.CommonException;

@Service
public class DashboardService {
	@Autowired private DashboardRepository repository;
	@Autowired private DashboardResourceAssembler assembler;
	
	public List<Resource<Dashboard>> getAllDashboards() {
		return repository.findAll().stream()
										.map(assembler::toResource)
										.collect(Collectors.toList());
	}

	public Resource<Dashboard> createDashboard(Dashboard dashboard) {
		return assembler.toResource(repository.save(dashboard));
	}
	
	public Resource<Dashboard> getDashboardById(String dashboardId) {
		Dashboard dashboard = repository.findById(dashboardId)
				.orElseThrow(() -> new CommonException("Could not find dashboard: " + dashboardId, 
													   HttpStatus.NOT_FOUND.value()));
		return assembler.toResource(dashboard);
	}

	public Resource<Dashboard> updateDashboard(String dashboardId, Dashboard newDashboard) {
		Dashboard updatedDashboard = repository.findById(dashboardId).map(dashboard -> {
			dashboard.setName(newDashboard.getName());
			return repository.save(dashboard);
		}).orElseThrow(() -> new CommonException("Could not find dashboard: " + dashboardId, 
												 HttpStatus.NOT_FOUND.value()));
		return assembler.toResource(updatedDashboard);
	}
	
	public void deleteDashboardById(String id) {
		repository.deleteById(id);
	}

}
