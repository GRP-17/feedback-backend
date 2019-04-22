package com.group17.label;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.group17.util.exception.CommonException;

/**
 * The {@link org.springframework.stereotype.Service} that will handle any retrieval
 * of {@link Label} related data.
 */
@Service
public class LabelService {
	/** Holds the instance of the LabelRepository which represents the database. */
	@Autowired private LabelRepository repository;
	/** Holds the instance of the factory which will make the resources. */
	@Autowired private LabelResourceAssembler assembler;

	/**
	 * Get every {@link Label} in the repository.
	 *
	 * @return all of the entries
	 */
	public List<Resource<Label>> getAllLabels() {
		return repository.findAll().stream()
										.map(assembler::toResource)
										.collect(Collectors.toList());
	}
	
	public List<Resource<Label>> getLabelsByDashboardId(String dashboardId) {
		return repository.findLabelsByDashboardId(dashboardId)
										.stream()
										.map(assembler::toResource)
										.collect(Collectors.toList());
	}
	
	public Label saveLabel(Label label) {
		return repository.save(label);
	}

	/**
	 * Save a {@link Label} entry to the database.
	 *
	 * @param label what to save
	 * @return the newly saved resource
	 */
	public Resource<Label> createLabel(Label label) {
		return assembler.toResource(saveLabel(label));
	}

	public Resource<Label> getLabelById(String labelId) {
		Label label = repository.findById(labelId)
							.orElseThrow(() -> new CommonException("Could not find label: " + labelId,
																   HttpStatus.NOT_FOUND.value()));
		return assembler.toResource(label);
	}
	
	/**
	 * Update a {@link Label} entry in the database.
	 *
	 * @param id the identifier of the entry to update
	 * @param newLabel the object containing the data to overwrite with
	 * @return the newly saved resource
	 * @throws CommonException if the id isn't valid (no entry with that given id)
	 */
	public Resource<Label> updateLabel(String labelId, Label newLabel) throws CommonException {

		Label updatedLabel = repository.findById(labelId).map(label -> {
			if(newLabel.getDashboardId() != null) {
				label.setDashboardId(newLabel.getDashboardId());
			}
			if(newLabel.getName() != null) {
				label.setName(newLabel.getName());
			}
			if(newLabel.getColor() != null) {
				label.setColor(newLabel.getColor());
			}
			return saveLabel(label);
		}).orElseThrow(() -> new CommonException("Could not find label: " + labelId,
												 HttpStatus.NOT_FOUND.value()));
		return assembler.toResource(updatedLabel);
	}

	/**
	 * Delete a {@link Label} entry in the database.
	 *
	 * @param id the id of the {@link Label to delete}
	 * @throws Exception if the id isn't valid (no entry with that given id)
	 */
	public void deleteLabelById(String id) throws Exception {
		repository.deleteById(id);
	}
	
}
