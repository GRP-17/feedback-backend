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

import com.group17.phrase.PhraseService;
import com.group17.phrase.blacklist.BlacklistedPhrase;
import com.group17.util.LoggerUtil;
import com.group17.util.exception.CommonException;

@CrossOrigin
@RestController
@RequestMapping(value = "/blacklistedphrases", produces = "application/hal+json")
public class BlacklistedPhrasesController {
	@Autowired private PhraseService phraseService;

	@GetMapping()
	public Resources<Resource<BlacklistedPhrase>> findAll() throws CommonException {
		List<Resource<BlacklistedPhrase>> resources = phraseService.getAllBlacklistedPhrases();
		LoggerUtil.log(Level.INFO, "[BlacklistedPhrase/Retrieve] Retrieved: "
										+ resources.size() + " phrases");
		return new Resources<Resource<BlacklistedPhrase>>(
				resources,
				linkTo(methodOn(BlacklistedPhrasesController.class).findAll())
					.withSelfRel());
	}

	@GetMapping("/{phraseId}")
	public Resource<BlacklistedPhrase> findOne(@PathVariable String phraseId) throws CommonException {
		Resource<BlacklistedPhrase> resource = phraseService.getBlacklistedPhraseById(phraseId);
		LoggerUtil.log(Level.INFO, "[BlacklistedPhrase/Retrieve] Retrieved: phrase " + phraseId);
		return resource;
	}

	@PostMapping(headers = "Accept=application/json")
	public ResponseEntity<?> create(@RequestBody BlacklistedPhrase newBlacklistedPhrase)
				throws URISyntaxException, TransactionSystemException {

		Resource<BlacklistedPhrase> resource = phraseService.createBlacklistedPhrase(newBlacklistedPhrase);
		LoggerUtil.log(Level.INFO, "[BlacklistedPhrase/Create] Created: " + newBlacklistedPhrase.getId());
		return ResponseEntity.created(new URI(resource.getId().expand(newBlacklistedPhrase.getId()).getHref())).body(resource);
	}

	@PutMapping("/{phraseId}")
	public ResponseEntity<?> update(@PathVariable String phraseId, @RequestBody BlacklistedPhrase newBlacklistedPhrase)
			throws URISyntaxException, TransactionSystemException {

		Resource<BlacklistedPhrase> resource = phraseService.updateBlacklistedPhrase(phraseId, newBlacklistedPhrase);
		LoggerUtil.log(Level.INFO, "[BlacklistedPhrase/Update] Updated: " + phraseId
										+ ". Object: " + newBlacklistedPhrase.toString());
		return ResponseEntity.created(new URI(resource.getId().expand(newBlacklistedPhrase.getId()).getHref())).body(resource);
	}

	@DeleteMapping("/{phraseId}")
	public ResponseEntity<?> delete(@PathVariable String phraseId) {

		try {
			phraseService.deleteBlacklistedPhraseById(phraseId);
			LoggerUtil.log(Level.INFO, "[BlacklistedPhrase/Delete] Deleted: " + phraseId);
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
