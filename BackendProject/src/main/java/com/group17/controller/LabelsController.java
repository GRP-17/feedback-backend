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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group17.label.Label;
import com.group17.label.LabelService;
import com.group17.util.LoggerUtil;
import com.group17.util.exception.CommonException;

@CrossOrigin
@RestController
@RequestMapping(value = "/labels", produces = "application/hal+json")
public class LabelsController {
	@Autowired private LabelService labelService;

	@GetMapping()
	public Resources<Resource<Label>> findAll(@RequestParam(value = "dashboardId")
													String dashboardId) throws CommonException {
		List<Resource<Label>> resources = labelService.getLabelsByDashboardId(dashboardId);
		LoggerUtil.log(Level.INFO, "[Label/Retrieve] Retrieved: "
										+ resources.size() + " labels");
		return new Resources<Resource<Label>>(
				resources,
				linkTo(methodOn(LabelsController.class).findAll(dashboardId))
					.withSelfRel());
	}

	@GetMapping("/{labelId}")
	public Resource<Label> findOne(@PathVariable String labelId) throws CommonException {
		Resource<Label> resource = labelService.getLabelById(labelId);
		LoggerUtil.log(Level.INFO, "[Label/Retrieve] Retrieved: label " + labelId);
		return resource;
	}

	@PostMapping(headers = "Accept=application/json")
	public ResponseEntity<?> create(@RequestBody Label newLabel)
				throws URISyntaxException, TransactionSystemException {

		Resource<Label> resource = labelService.createLabel(newLabel);
		LoggerUtil.log(Level.INFO, "[Label/Create] Created: " + newLabel.getLabelId());
		return ResponseEntity.created(new URI(resource.getId().expand(newLabel.getLabelId()).getHref())).body(resource);
	}

	@PutMapping("/{labelId}")
	public ResponseEntity<?> update(@PathVariable String labelId, @RequestBody Label newLabel)
			throws URISyntaxException, TransactionSystemException {

		Resource<Label> resource = labelService.updateLabel(labelId, newLabel);
		LoggerUtil.log(Level.INFO, "[Label/Update] Updated: " + labelId
										+ ". Object: " + newLabel.toString());
		return ResponseEntity.created(new URI(resource.getId().expand(newLabel.getLabelId()).getHref())).body(resource);
	}

	@DeleteMapping("/{labelId}")
	public ResponseEntity<?> delete(@PathVariable String labelId) {

		try {
			labelService.deleteLabelById(labelId);
			LoggerUtil.log(Level.INFO, "[Label/Delete] Deleted: " + labelId);
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
	@ExceptionHandler(CommonException.class)
	public ResponseEntity<String> exceptionHandler(CommonException ex) {
		LoggerUtil.logException(ex);
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.valueOf(ex.getErrorCode()));
	}

}
