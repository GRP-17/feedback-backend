package com.group17.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group17.dashboard.Dashboard;
import com.group17.dashboard.DashboardService;
import com.group17.util.LoggerUtil;
import com.group17.util.exception.CommonException;

@CrossOrigin
@RestController
@RequestMapping(value = "/dashboards", produces = "application/hal+json")
public class DashboardsController {
	@Autowired private DashboardService dashboardService;

	@GetMapping()
	public Resources<Resource<Dashboard>> findAll() throws CommonException {
		List<Resource<Dashboard>> resources = dashboardService.getAllDashboards();
		LoggerUtil.log(Level.INFO, "[Dashboard/Retrieve] Retrieved: " 
										+ resources.size() + " dashboards");
		return new Resources<Resource<Dashboard>>(
				resources,
				linkTo(methodOn(DashboardsController.class).findAll())
					.withSelfRel());
	}
	
	@PostMapping(headers = "Accept=application/json")
	public ResponseEntity<?> create(@RequestBody Dashboard newDashboard)
			throws URISyntaxException, TransactionSystemException {
		Resource<Dashboard> resource = dashboardService.createDashboard(newDashboard);

		LoggerUtil.log(Level.INFO, "[Dashboard/Create] Created: " + newDashboard.getId());
		return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
	}
	
	@PutMapping("/{dashboardId}")
	public ResponseEntity<?> update(@RequestBody Dashboard newDashboard, @PathVariable String dashboardId)
			throws URISyntaxException, TransactionSystemException {

		Resource<Dashboard> resource = dashboardService.updateDashboard(dashboardId, newDashboard);
		LoggerUtil.log(Level.INFO, "[Feedback/Update] Updated: " + dashboardId
										+ ". Object: " + newDashboard.toString());
		return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
	}

	@DeleteMapping("/{dashboardId}")
	public ResponseEntity<?> delete(@PathVariable String dashboardId) {

		try {
			dashboardService.deleteDashboardById(dashboardId);
			LoggerUtil.log(Level.INFO, "[Dashboard/Delete] Deleted: " + dashboardId);
		} catch (Exception e) {
		}

		return ResponseEntity.noContent().build();
	}
	
	/**
	 * Handles any CommonExceptions thrown.
	 *
	 * @param ex the exception that was thrown
	 * @return a response entity with the message and HTTP status code
	 */
	/* Error Handlers */
	@ExceptionHandler(CommonException.class)
	public ResponseEntity<String> exceptionHandler(CommonException ex) {
		LoggerUtil.logException(ex);
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.valueOf(ex.getErrorCode()));
	}

}
