package com.group17.controller;

import static com.group17.util.Constants.DEFAULT_COMMON_PHRASES_AMOUNT;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group17.feedback.Feedback;
import com.group17.feedback.FeedbackService;
import com.group17.negativeperday.NegativePerDayService;
import com.group17.ngram.NGramService;
import com.group17.ngram.termvector.TermVector;
import com.group17.tone.Sentiment;
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
	@Autowired private FeedbackService feedbackService;
	@Autowired private NGramService ngramService;

	@Autowired private NegativePerDayService negativePerDayService;

	/**
	 * the default mapping for a get request to the feedback endpoint
	 *
	 * @return all feedback from the database, returned as resources
	 */
	@GetMapping()
	public Resources<Resource<Feedback>> findAll() {
		List<Resource<Feedback>> resources = feedbackService.getAllFeedback();
		LoggerUtil.log(Level.INFO, "[Feedback/Browse] Found " + resources.size() 
										+ " resources in the repository");

		return new Resources<>(
				resources,
				linkTo(methodOn(FeedbackController.class).findAll()).withSelfRel(),
				linkTo(methodOn(FeedbackController.class).getCount()).withRel("count"),
				linkTo(methodOn(FeedbackController.class).getPaged(-1, -1)).withRel("paged"),
				linkTo(methodOn(FeedbackController.class).getSentimentsCount()).withRel("sentiment_count"),
				linkTo(methodOn(FeedbackController.class).getAverageRating()).withRel("rating_average"),
				linkTo(methodOn(FeedbackController.class).getStarRatingCount()).withRel("rating_count"),
				linkTo(methodOn(FeedbackController.class).getNegativePerDay()).withRel("rating_negative"),
				linkTo(methodOn(FeedbackController.class).getCommonPhrases()).withRel("common_phrases"));
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
		LoggerUtil.log(Level.INFO, "[Feedback/Retrieve] Retrieved: " + id
										+ ". Object: " + resource.getContent().toString());
		return resource;
	}

	/**
	 * a mapping for get requests
	 * will return feedback paginated
	 *
	 * @param indexTo
	 * @param indexFrom
	 * @return the resource for the page given
	 */
	@GetMapping("/paged")
	public Resources<Resource<Feedback>> getPaged(@PathVariable int page,
												  @PathVariable int pageSize)
	{
		List<Resource<Feedback>> resource = feedbackService.getPagedFeedback(page, pageSize);
		LoggerUtil.log(Level.INFO, "[Feedback/Retrieve] Retrieved: " + pageSize
				+ " elements on page " + page);
		return new Resources<Resource<Feedback>>(resource);
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

		// N-Grams
		ngramService.onFeedbackCreated(newFeedback);
		
    	if (newFeedback.getSentimentEnum().equals(Sentiment.NEGATIVE)) {
    		LoggerUtil.log(Level.INFO, "[Feedback/Analysis] A Negative review was left for id " 
    										+ newFeedback.getId());
    		
			// Increase negative rating this day
			negativePerDayService.increaseNegativeByDate(newFeedback.getCreated());
    	}
		// N-Grams
		ngramService.onFeedbackCreated(newFeedback);

		LoggerUtil.log(Level.INFO, "[Feedback/Create] Created: " + newFeedback.getId()
										+ ". Object: " + newFeedback.toString());
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
		LoggerUtil.log(Level.INFO, "[Feedback/Update] Updated: " + id
										+ ". Object: " + newFeedback.toString());
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
			ngramService.onFeedbackRemoved(id);
			LoggerUtil.log(Level.INFO, "[Feedback/Delete] Deleted: " + id);
		} catch (Exception e) {
		}

		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/count")
	public ResponseEntity<?> getCount() throws CommonException {
		long count = feedbackService.getCount();
		Map<String, Long> res = new HashMap<String, Long>();
		res.put("count", count);
		
		LoggerUtil.log(Level.INFO, "[Feedback/Count] Counted: " + count);
		
		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(res));
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize feedback count", 
									  HttpStatus.NO_CONTENT.value());
		}
	}

	@GetMapping("/sentiments/count")
	public ResponseEntity<?> getSentimentsCount() throws CommonException {
		Map<Sentiment, Long> counts = feedbackService.getSentimentCounts();
		
		try {
			String countsAsString = new ObjectMapper().writeValueAsString(counts);
			LoggerUtil.log(Level.INFO, 
						   "[Feedback/SentimentCount] Counted: " + countsAsString);
			
			return ResponseEntity.ok(countsAsString);
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize sentiment counts", 
									  HttpStatus.NO_CONTENT.value());
		}
	}

	@GetMapping("/rating/count")
	public ResponseEntity<?> getStarRatingCount() throws CommonException {
		Map<Integer, Long> ratings = feedbackService.getRatingCounts();
		
		try {
			String ratingsAsString = new ObjectMapper().writeValueAsString(ratings);
			LoggerUtil.log(Level.INFO, 
						   "[Feedback/RatingCount] Counted: " + ratingsAsString);
			
			return ResponseEntity.ok(ratingsAsString);
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize star rating counts", 
									  HttpStatus.NO_CONTENT.value());
		}
	}
	
	@GetMapping("/rating/average")
	public ResponseEntity<?> getAverageRating() throws CommonException {
		double average = feedbackService.getAverageRating(true);
		Map<String, Double> map = new HashMap<String, Double>();
		map.put("average", average);
		
		LoggerUtil.log(Level.INFO, 
					   "[Feedback/RatingAverage] Calculated Average: " + average);
		
		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(map));
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize average rating", 
									  HttpStatus.NO_CONTENT.value());
		}
	}
	
	@GetMapping("/rating/negativeperday")
	public ResponseEntity<?> getNegativePerDay() throws CommonException {
		Map<String, Object> map = negativePerDayService.findNegativePerDay();
		
		LoggerUtil.log(Level.INFO, 
					"[Feedback/RatingNegativePerDay] Returned " + map.size() + " days");
		
		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(map));
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize negative rating counts", 
									HttpStatus.NO_CONTENT.value());
		}
	}

	@GetMapping("/commonphrases")
	public ResponseEntity<?> getCommonPhrases() throws CommonException {

		Map<String, Collection<TermVector>> map 
						= feedbackService.getCommonPhrases(DEFAULT_COMMON_PHRASES_AMOUNT);
		
		LoggerUtil.log(Level.INFO, 
					   "[Feedback/RatingAverage] Returned " + map.size() + " phrases");
		
		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(map));
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize average rating", 
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
