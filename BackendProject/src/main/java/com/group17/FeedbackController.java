package com.group17;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group17.util.CommonException;
import com.group17.util.LoggerUtil;

/**
 * Handles the feedback endpoint and any child/sub endpoints of it
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/feedback", produces = "application/hal+json")
public class FeedbackController {
	/**
	 * holds the instance of the FeedbackService
	 */
	@Autowired
	private FeedbackService feedbackService;

	/**
	 * the default mapping for a get request to the feedback endpoint
	 *
	 * @return all feedback from the database, returned as resources
	 */
	@GetMapping()
	public Resources<Resource<Feedback>> findAll() {

		List<Resource<Feedback>> resources = feedbackService.getAllFeedback();
		LoggerUtil.logFeedbackFindAll(resources.size());

		return new Resources<>(
				resources,
				linkTo(methodOn(FeedbackController.class).findAll()).withSelfRel(),
				linkTo(methodOn(FeedbackController.class).getCount()).withRel("count"),
				linkTo(methodOn(FeedbackController.class).getSentimentsCount()).withRel("sentiment_count"),
				linkTo(methodOn(FeedbackController.class).getAverageRating()).withRel("rating_average"),
				linkTo(methodOn(FeedbackController.class).getStarRatingCount()).withRel("rating_count"));
	}

	/**
	 * a sub-mapping for get requests
	 * will return a resource for one piece of feedback from the database
	 *
	 * @param id the id of the feedback to return
	 * @return the resource for the id given, if one exist
	 * @throws CommonException thrown if an entry in the database could not be found for that id
	 */
	@GetMapping("/{id}")
	public Resource<Feedback> findOne(@PathVariable String id) throws CommonException {

		Resource<Feedback> resource = feedbackService.getFeedbackById(id);
		LoggerUtil.logFeedbackFindOne(resource.getContent());
		return resource;
	}

	@GetMapping("/count")
	public ResponseEntity<?> getCount() throws CommonException {
		long count = feedbackService.getCount();
		Map<String, Long> res = new HashMap<String, Long>();
		res.put("count", count);
		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(res));
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize feedback count", HttpStatus.NO_CONTENT.value());
		}
	}

	@GetMapping("/sentiments/count")
	public ResponseEntity<?> getSentimentsCount() throws CommonException {
		Map<Sentiment, Long> counts = new HashMap<Sentiment, Long>();
		for (Sentiment sentiment : Sentiment.values()) {
			counts.put(sentiment, feedbackService.getCountBySentiment(sentiment.toString()));
		}
		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(counts));
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize sentiment counts", HttpStatus.NO_CONTENT.value());
		}
	}

	/**
	 * default mapping for a post request to the feedback endpoint
	 *
	 * @param newFeedback the body of the post request (should be in JSON format)
	 * @return a HTTP status of 201 'Created' and a link to the resource they just created (the endpoint to get that resource)
	 * @throws URISyntaxException
	 * @throws TransactionSystemException will be thrown when the body of the request is not as expected (JSON format with a rating)
	 */
	@PostMapping(headers = "Accept=application/json")
	public ResponseEntity<?> create(@RequestBody Feedback newFeedback)
			throws URISyntaxException, TransactionSystemException {

		Resource<Feedback> resource = feedbackService.createFeedback(newFeedback);
		LoggerUtil.logFeedbackCreate(newFeedback);

		return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
	}

	/**
	 * a sub-mapping for put requests
	 *
	 * @param newFeedback the new feedback to save
	 * @param id          the id of the feedback to update
	 * @return a resource with the updated feedback
	 * @throws URISyntaxException
	 * @throws TransactionSystemException will be thrown when the body of the request is not as expected (JSON format with a rating)
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@RequestBody Feedback newFeedback, @PathVariable String id)
			throws URISyntaxException, TransactionSystemException {

		Resource<Feedback> resource = feedbackService.updateFeedback(id, newFeedback);
		LoggerUtil.logFeedbackUpdate(resource.getContent());
		return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
	}

	/**
	 * a sub-mapping for delete requests
	 *
	 * @param id the id of the feedback to be deleted
	 * @return an empty response
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {

		try {
			feedbackService.deleteFeedbackById(id);
			LoggerUtil.logFeedbackDeleted(id);
		} catch (Exception e) {
		}

		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/rating/average")
	public ResponseEntity<?> getAverageRating() throws CommonException {
		Map<String, Double> map = new HashMap<String, Double>();
		
		// The unformatted average - it could have many decimal values
		double averageU = feedbackService.getAverageRating();
		// The formatted average - trimmed of unnecessary decimal values
		double averageF = Double.valueOf(new DecimalFormat("#.##")
												.format(averageU));
		
		map.put("average", averageF);
		
		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(map));
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize average rating", HttpStatus.NO_CONTENT.value());
		}
	}

	@GetMapping("/rating/count")
	public ResponseEntity<?> getStarRatingCount() throws CommonException {
		// Key: the ratings [1..5], Value: The count of this rating
		Map<Integer, Long> ratings = new HashMap<Integer, Long>();

		for(int rating = 1; rating <= 5; rating++){
		    ratings.put(rating, feedbackService.getCountByRating(rating));
        }
		
		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(ratings));
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize star rating counts", 
									  HttpStatus.NO_CONTENT.value());
		}
	}

	/**
	 * handles any CommonExceptions thrown
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
	
	@ExceptionHandler(JsonParseException.class)
	public ResponseEntity<String> exceptionHandler(JsonParseException ex) {
		LoggerUtil.logException(ex);
		return new ResponseEntity<String>("Unable to parse Feedback object", 
										  HttpStatus.BAD_REQUEST);
	}

	/**
	 * handles any TransactionSystemExceptions
	 *
	 * @param ex the exception that was thrown
	 * @return a response entity with the message and HTTP status 412 'Precondition_failed'
	 */
	@ExceptionHandler(TransactionSystemException.class)
	public ResponseEntity<String> constraintViolationExceptionHandler(
			TransactionSystemException ex) {

		// Loop until find the ConstraintViolationException
		Throwable t = ex;
		while ((t != null) && !(t instanceof ConstraintViolationException)) {
			t = t.getCause();
		}

		StringBuilder msgList = new StringBuilder();
		if (t != null && t instanceof ConstraintViolationException) {
			// Loop the list to get results
			for (ConstraintViolation<?> constraintViolation :
					((ConstraintViolationException) t).getConstraintViolations()) {
				msgList.append(constraintViolation.getMessage());
			}
		}
		
		return new ResponseEntity<>(msgList.toString(), HttpStatus.PRECONDITION_FAILED);
	}
}
