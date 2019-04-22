package com.group17.dashboard;

import com.group17.util.exception.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@link org.springframework.stereotype.Service} that will handle any retrieval
 * of {@link Dashboard} related data.
 */
@Service
public class DashboardService {
	/**
	 * Holds the instance of the DashboardRepository which represents the database.
	 */
	@Autowired
	private DashboardRepository repository;
	/**
	 * Holds the instance of the factory which will make the resources.
	 */
	@Autowired
	private DashboardResourceAssembler assembler;

	/**
	 * Get every {@link Dashboard} in the repository.
	 *
	 * @return all of the entries
	 */
	public List<Resource<Dashboard>> getAllDashboards() {
		return repository.findAll().stream()
				.map(assembler::toResource)
				.collect(Collectors.toList());
	}

	/**
	 * Save a {@link Dashboard} entry to the database.
	 *
	 * @param dashboard what to save
	 * @return the newly saved resource
	 */
	public Resource<Dashboard> createDashboard(Dashboard dashboard) {
		return assembler.toResource(repository.save(dashboard));
	}

	public Resource<Dashboard> getDashboardById(String dashboardId) {
		Dashboard dashboard = repository.findById(dashboardId)
				.orElseThrow(() -> new CommonException("Could not find dashboard: " + dashboardId,
						HttpStatus.NOT_FOUND.value()));
		return assembler.toResource(dashboard);
	}

	/**
	 * Update a {@link Dashboard} entry in the database.
	 *
	 * @param id           the identifier of the entry to update
	 * @param newDashboard the object containing the data to overwrite with
	 * @return the newly saved resource
	 * @throws CommonException if the id isn't valid (no entry with that given id)
	 */
	public Resource<Dashboard> updateDashboard(String dashboardId, Dashboard newDashboard) throws CommonException {

		Dashboard updatedDashboard = repository.findById(dashboardId).map(dashboard -> {
			if (newDashboard.getName() != null) {
				dashboard.setName(newDashboard.getName());
			}
			return repository.save(dashboard);
		}).orElseThrow(() -> new CommonException("Could not find dashboard: " + dashboardId,
				HttpStatus.NOT_FOUND.value()));
		return assembler.toResource(updatedDashboard);
	}

	/**
	 * Delete a {@link Dashboard} entry in the database.
	 *
	 * @param id the id of the {@link Dashboard to delete}
	 * @throws Exception if the id isn't valid (no entry with that given id)
	 */
	public void deleteDashboardById(String id) throws Exception {
		repository.deleteById(id);
	}

}
