package com.group17.label;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import com.group17.controller.LabelsController;

/**
 * Used as a factory for the {@link Resource}s.
 */
@Component
public class LabelResourceAssembler implements ResourceAssembler<Label, Resource<Label>> {

	/**
	 * Generates a resource from the Label object supplied
	 * @param label the object to make into a resource
	 * @return a resource object wrapping up the label and has links to the label itself
	 */
	@Override
	public Resource<Label> toResource(Label label) {
		return new Resource<Label>(
				label,
				linkTo(methodOn(LabelsController.class)
							.findOne(label.getLabelId()))
					.withSelfRel());
	}
	
}
