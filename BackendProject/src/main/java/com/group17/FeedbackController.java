package com.group17;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import com.group17.util.CommonException;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/feedback", produces = "application/hal+json")
public class FeedbackController {

	private final FeedbackRepository repository;
	private final FeedbackResourceAssembler assembler;
	private final AnalysisService analysisService;

	public FeedbackController(FeedbackRepository repository, FeedbackResourceAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
		this.analysisService = new AnalysisService(
				"t6ayyh6UX9UiRiM-SFCkjSOXHdasKGJbiguzWEvu8yUV",
				"2018-11-27",
				"https://gateway-wdc.watsonplatform.net/tone-analyzer/api"
		);
	}

	@GetMapping()
	public Resources<Resource<Feedback>> findAll() {

		List<Resource<Feedback>> feedback =
				repository.findAll().stream().map(assembler::toResource).collect(Collectors.toList());

		Application.getLogger().info("[feedback/browse] Found. Object list size: " + feedback.size());

		return new Resources<>(
				feedback, linkTo(methodOn(FeedbackController.class).findAll()).withSelfRel());
	}

	@PostMapping(headers = "Accept=application/json")
	public ResponseEntity<?> create(@RequestBody Feedback newFeedback)
			throws URISyntaxException, TransactionSystemException {

		Resource<Feedback> resource = assembler.toResource(repository.save(newFeedback));
		Application.getLogger().info("[feedback/create] Created: " + newFeedback.getId()
										+ ". Object: " + newFeedback.toString());
		if(newFeedback.getText().length() != 0) {
			analysisService.analyze(newFeedback.getText());
		}

		return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
	}

	@GetMapping("/{id}")
	public Resource<Feedback> findOne(@PathVariable String id) throws CommonException {

		Feedback feedback =
				repository
				.findById(id)
				.orElseThrow(
						() ->
						new CommonException(
								"Could not find feedback: " + id, HttpStatus.NOT_FOUND.value()));

		Application.getLogger().info("[feedback/retrieve] Retrieved: " + id 
										+ ". Object: " + feedback.toString());

		return assembler.toResource(feedback);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@RequestBody Feedback newFeedback, @PathVariable String id)
			throws URISyntaxException, TransactionSystemException {
		Feedback updatedFeedback =
				repository
				.findById(id)
				.map(feedback -> {
						Integer newRating = newFeedback.getRating();
						String newText = newFeedback.getText();
						if (newRating != null) {
							feedback.setRating(newRating);
						}
						if (newText != null) {
							feedback.setText(newText);
						}
						return repository.save(feedback);
					 })
				.orElseThrow(
						() ->
						new CommonException(
								"Could not find feedback: " + id, HttpStatus.NOT_FOUND.value()));

		Application.getLogger().info("[feedback/update] Updated: " + id 
										+ ". Object: " + newFeedback.toString());

		Resource<Feedback> resource = assembler.toResource(updatedFeedback);

		return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {

		try {
			repository.deleteById(id);
			Application.getLogger().info("[feedback/delete] Deleted: " + id);
		} catch (Exception e) {
			throw new CommonException("Could not find feedback: " + id, HttpStatus.NOT_FOUND.value());
		}

		return ResponseEntity.noContent().build();
	}

	/* Error Handlers */
	// TODO: could be extended from a common controller class
	@ExceptionHandler(CommonException.class)
	public ResponseEntity<String> exceptionHandler(CommonException ex) {
		Application.getLogger().warn("[exception] " + ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.valueOf(ex.getErrorCode()));
	}

	@ExceptionHandler(TransactionSystemException.class)
	public ResponseEntity<String> constraintViolationExceptionHandler(
			TransactionSystemException ex) {

		// loop until find the ConstraintViolationException
		Throwable t = ex;
		while ((t != null) && !(t instanceof ConstraintViolationException)) {
			t = t.getCause();
		}

		// loop the list to get results
		StringBuilder msgList = new StringBuilder();
		for (ConstraintViolation<?> constraintViolation :
			((ConstraintViolationException) t).getConstraintViolations()) {
			msgList.append(constraintViolation.getMessage());
		}

		return new ResponseEntity<>(msgList.toString(), HttpStatus.PRECONDITION_FAILED);
	}
}
