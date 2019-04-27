package com.group17.phrase.blacklist;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import com.group17.controller.BlacklistedPhrasesController;

/**
 * Used as a factory for the {@link BlacklistedPhrase}s.
 */
@Component
public class BlacklistedPhraseResourceAssembler 
				implements ResourceAssembler<BlacklistedPhrase, 
											 Resource<BlacklistedPhrase>> {

	/**
	 * Generates a resource from the BlacklistedPhrase object supplied.
	 * 
	 * @param phrase the object to make into a resource
	 * @return a resource object wrapping up the label and has links to the label itself
	 */
	@Override
	public Resource<BlacklistedPhrase> toResource(BlacklistedPhrase phrase) {
		return new Resource<BlacklistedPhrase>(
				phrase,
				linkTo(methodOn(BlacklistedPhrasesController.class)
							.findOne(phrase.getId()))
					.withSelfRel());
	}
	
}
