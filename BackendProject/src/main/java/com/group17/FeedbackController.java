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

/**
 * Handles the feedback endpoint and any child/sub endpoints of it
 */
@RestController
@RequestMapping(value = "/feedback", produces = "application/hal+json")
public class FeedbackController {

	/** holds the instance of the FeedbackRepository which represents the database */
	private final FeedbackRepository repository;
	/** holds the instance of the factory which will make the resources */
	private final FeedbackResourceAssembler assembler;
	/** holds the instance of the AnalysisService  */
	private final AnalysisService analysisService;

	/**
	 * Constructor
	 * The Spring server will automatically create an instance of this class using its constructor
	 * @param repository instance created automatically by the Spring server and assigned to the member variable
	 * @param assembler instance created automatically by the Spring server and assigned to the member variable
	 */
	public FeedbackController(FeedbackRepository repository, FeedbackResourceAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
		this.analysisService = new AnalysisService(
				"t6ayyh6UX9UiRiM-SFCkjSOXHdasKGJbiguzWEvu8yUV",
				"2018-11-27",
				"https://gateway-wdc.watsonplatform.net/tone-analyzer/api"
		);
	}

	/**
	 * the default mapping for a get request to the feedback endpoint
	 * @return all feedback from the database, returned as resources
	 */
	@GetMapping()
	public Resources<Resource<Feedback>> findAll() {

		List<Resource<Feedback>> feedback =
				repository.findAll().stream().map(assembler::toResource).collect(Collectors.toList());

		Application.getLogger().info("[feedback/browse] Found. Object list size: " + feedback.size());

		return new Resources<>(
				feedback, linkTo(methodOn(FeedbackController.class).findAll()).withSelfRel());
	}

	/**
	 * default mapping for a post request to the feedback endpoint
	 * @param newFeedback the body of the post request (should be in JSON format)
	 * @return a HTTP status of 201 'Created' and a link to the resource they just created (the endpoint to get that resource)
	 * @throws URISyntaxException
	 * @throws TransactionSystemException will be thrown when the body of the request is not as expected (JSON format with a rating)
	 */
	@CrossOrigin
	@PostMapping(headers = "Accept=application/json")
	public ResponseEntity<?> create(@RequestBody Feedback newFeedback)
			throws URISyntaxException, TransactionSystemException {

		Resource<Feedback> resource = assembler.toResource(repository.save(newFeedback));
		Application.getLogger().info("[feedback/create] Created: " + newFeedback.getId()
										+ ". Object: " + newFeedback.toString());
		if(newFeedback.getText().length() != 0) {
			ToneAnalysis toneAnalysis = analysisService.analyze(newFeedback.getText());
			Application.getLogger().info(toneAnalysis);
		}

		return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
	}

	/**
	 * a sub-mapping for get requests
	 * will return a resource for one piece of feedback from the database
	 * @param id the id of the feedback to return
	 * @return the resource for the id given, if one exist
	 * @throws CommonException thrown if an entry in the database could not be found for that id
	 */
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

	/**
	 * a sub-mapping for put requests
	 * @param newFeedback the new feedback to save
	 * @param id the id of the feedback to update
	 * @return a resource with the updated feedback
	 * @throws URISyntaxException
	 * @throws TransactionSystemException will be thrown when the body of the request is not as expected (JSON format with a rating)
	 */
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

	/**
	 * a sub-mapping for delete requests
	 * @param id the id of the feedback to be deleted
	 * @return an empty response
	 */
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

	/**
	 * handles any CommonExceptions thrown
	 * @param ex the exception that was thrown
	 * @return a response entity with the message and HTTP status code
	 */
	/* Error Handlers */
	// TODO: could be extended from a common controller class
	@ExceptionHandler(CommonException.class)
	public ResponseEntity<String> exceptionHandler(CommonException ex) {
		Application.getLogger().warn("[exception] " + ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.valueOf(ex.getErrorCode()));
	}

	/**
	 * handles any TransactionSystemExceptions
	 * @param ex the exception that was thrown
	 * @return a response entity with the message and HTTP status 412 'Precondition_failed'
	 */
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
