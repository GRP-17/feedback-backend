package com.group17.controller;

import static com.group17.util.Constants.COMMON_PHRASES_AMOUNT;
import static com.group17.util.Constants.DASHBOARD_FEEDBACK_PAGE;
import static com.group17.util.Constants.PARAM_DEFAULT_STRING;
import static com.group17.util.Constants.PARAM_DEFAULT_LONG;
import static com.group17.util.Constants.FEEDBACK_MIN_RATING;
import static com.group17.util.Constants.FEEDBACK_MAX_RATING;
import static com.group17.util.Constants.PARAM_DEFAULT_INTEGER;
import static com.group17.util.Constants.DASHBOARD_FEEDBACK_PAGE_SIZE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group17.dashboard.DashboardService;
import com.group17.feedback.StatType;
import com.group17.feedback.filter.Filters;
import com.group17.feedback.tone.Sentiment;
import com.group17.feedback.Feedback;
import com.group17.feedback.FeedbackService;
import com.group17.negativeperday.NegativePerDayService;
import com.group17.ngram.NGramService;
import com.group17.ngram.termvector.TermVector;
import com.group17.util.Constants;
import com.group17.util.LoggerUtil;
import com.group17.util.exception.CommonException;

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
	@Autowired private NegativePerDayService negativePerDayService;
	@Autowired private DashboardService dashboardService;
	@Autowired private NGramService ngramService;

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
			negativePerDayService.incrementNegativeByDate(newFeedback.getDashboardId(),
														 newFeedback.getCreated());
		}
		
		LoggerUtil.log(Level.INFO, "[Feedback/Create] Created: " + newFeedback.getId()
										+ ". Object: " + newFeedback.toString());
		return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
	}

	/**
	 * a sub-mapping for put requests
	 * @param id          the id of the feedback to update
	 * @return a resource with the updated feedback
	 * @throws URISyntaxException
	 * @throws TransactionSystemException will be thrown when the body of the request is not as expected (JSON format with a rating)
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable String id,
			 						@RequestParam(value = "dashboardId", required = false, defaultValue = PARAM_DEFAULT_STRING)
											String dashboardId,
			 						@RequestParam(value = "rating", required = false, defaultValue = PARAM_DEFAULT_INTEGER)
											int rating,
			 						@RequestParam(value = "text", required = false, defaultValue = PARAM_DEFAULT_STRING)
											String text,
			 						@RequestParam(value = "pinned", required = false, defaultValue = PARAM_DEFAULT_STRING)
											String pinned)
			throws URISyntaxException, TransactionSystemException {

		Feedback newFeedback = feedbackService.getFeedbackById(id).getContent();
		if(dashboardId != null && !dashboardId.equals(Constants.PARAM_DEFAULT_STRING)) {
			newFeedback.setDashboardId(dashboardId);
		}
		if(rating != Constants.PARAM_DEFAULT_INTEGER_VALUE
				&& rating >= FEEDBACK_MIN_RATING 
				&& rating <= FEEDBACK_MAX_RATING) {
			newFeedback.setRating(rating);
		}
		if(text != null && !text.equals(Constants.PARAM_DEFAULT_STRING)) {
			newFeedback.setText(text);
			feedbackService.getWatsonGateway().deduceAndSetSentiment(newFeedback);
		}
		if(pinned != null && !pinned.equals(Constants.PARAM_DEFAULT_STRING)) {
			boolean booleanValue = Boolean.valueOf(pinned);
			newFeedback.setPinned(booleanValue);
		}
		
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
	
	/**
	 * a mapping for get requests
	 * will return feedback paginated
	 *
	 * @param page
	 * @param pageSize
	 * @return the resource for the page given
	 */
	@GetMapping()
	public Resources<Resource<Feedback>> findFeedback(
				 @RequestParam(value = "dashboardId") 
						String dashboardId,
				 @RequestParam(value = "page", required = false, defaultValue = "1") 
				 		int page,
				 @RequestParam(value = "pageSize", required = false, defaultValue = "10") 
				 		int pageSize,
				 @RequestParam(value = "query", required = false, defaultValue = PARAM_DEFAULT_STRING)
						String query,
				 @RequestParam(value = "since", required = false, defaultValue = PARAM_DEFAULT_LONG)
						long since,
				 @RequestParam(value = "sentiment", required = false, defaultValue = PARAM_DEFAULT_STRING)
						String sentiment) {
		
		Filters filters = Filters.fromParameters(dashboardId, query, since, sentiment);
		List<Resource<Feedback>> resources = feedbackService.getPagedFeedback(filters, page, pageSize);
		LoggerUtil.log(Level.INFO, "[Feedback/Retrieve] Retrieved: " + resources.size()
				+ " elements on page " + page);
		return new Resources<Resource<Feedback>>(
				resources,
				linkTo(methodOn(FeedbackController.class).findFeedback(dashboardId, -1, -1, query, since, sentiment))
					.withSelfRel(),
				linkTo(methodOn(FeedbackController.class).getCount(dashboardId, query, since, sentiment))
					.withRel("count"),
				linkTo(methodOn(FeedbackController.class).getSentimentsCount(dashboardId, query, since, sentiment))
					.withRel("sentiment_count"),
				linkTo(methodOn(FeedbackController.class).getAverageRating(dashboardId, query, since, sentiment))
					.withRel("rating_average"),
				linkTo(methodOn(FeedbackController.class).getStarRatingCount(dashboardId, query, since, sentiment))
					.withRel("rating_count"),
				linkTo(methodOn(FeedbackController.class).getNegativePerDay(dashboardId, query, since, sentiment))
					.withRel("rating_negative"),
				linkTo(methodOn(FeedbackController.class).getCommonPhrases(dashboardId, query, since, sentiment))
					.withRel("common_phrases"));
	}
	
	@GetMapping("/stats")
	public ResponseEntity<?> stats(
			 @RequestParam(value = "dashboardId") 
					String dashboardId,
			 @RequestParam(value = "query", required = false, defaultValue = PARAM_DEFAULT_STRING)
					String query,
			 @RequestParam(value = "since", required = false, defaultValue = PARAM_DEFAULT_LONG)
					long since,
			 @RequestParam(value = "sentiment", required = false, defaultValue = PARAM_DEFAULT_STRING)
					String sentiment) throws CommonException {

		Filters filters = Filters.fromParameters(dashboardId, query, since, sentiment);
		Map<String, Object> map = new HashMap<String, Object>();

		for(StatType endpoint : StatType.values()) {
			String key = endpoint.getJsonKey();

			switch(endpoint) {
			case DASHBOARD_NAME:
				map.put(key, dashboardService.getDashboardById(dashboardId).getContent().getName());
				break;
			case FEEDBACK:
				map.put(key, feedbackService.getPagedFeedback(filters.clone(),
															  DASHBOARD_FEEDBACK_PAGE,
															  DASHBOARD_FEEDBACK_PAGE_SIZE));
				break;
			case FEEDBACK_COUNT:
				map.put(key, feedbackService.getFeedbackCount(filters.clone()));
				break;
			case FEEDBACK_SENTIMENT_COUNT:
				map.put(key, feedbackService.getSentimentCounts(filters.clone()));
				break;
			case FEEDBACK_RATING_COUNT:
				map.put(key, feedbackService.getRatingCounts(filters.clone()));
				break;
			case FEEDBACK_RATING_AVERAGE:
				map.put(key, feedbackService.getAverageRating(filters.clone(), true));
				break;
			case FEEDBACK_RATING_NEGATIVE:
				map.put(key, negativePerDayService.findNegativePerDay(filters.clone()));
				break;
			case FEEDBACK_COMMON_PHRASES:
				map.put(key, feedbackService.getCommonPhrases(filters.clone(), 
															  COMMON_PHRASES_AMOUNT));
				break;
			}
		}

		LoggerUtil.log(Level.INFO, "[Root/Dashboard] Returned " + map.size() + " values");

		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(map));
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize endpoints",
									  HttpStatus.NO_CONTENT.value());
		}
	}

	@GetMapping("/count")
	public ResponseEntity<?> getCount(
			 	@RequestParam(value = "dashboardId") 
			 		String dashboardId,
				@RequestParam(value = "query", required = false, defaultValue = PARAM_DEFAULT_STRING)
			 		String query,
				@RequestParam(value = "since", required = false, defaultValue = PARAM_DEFAULT_LONG)
			 		long since,
				@RequestParam(value = "sentiment", required = false, defaultValue = PARAM_DEFAULT_STRING)
			 		String sentiment) throws CommonException {

		Filters filters = Filters.fromParameters(dashboardId, query, since, sentiment);
		long count = feedbackService.getFeedbackCount(filters);
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
	public ResponseEntity<?> getSentimentsCount(
			 	@RequestParam(value = "dashboardId") 
		 			String dashboardId,
		 		@RequestParam(value = "query", required = false, defaultValue = PARAM_DEFAULT_STRING)
		 			String query,
		 		@RequestParam(value = "since", required = false, defaultValue = PARAM_DEFAULT_LONG)
		 			long since,
		 		@RequestParam(value = "sentiment", required = false, defaultValue = PARAM_DEFAULT_STRING)
		 			String sentiment) throws CommonException {

		Filters filters = Filters.fromParameters(dashboardId, query, since, sentiment);
		Map<Sentiment, Long> counts = feedbackService.getSentimentCounts(filters);

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
	public ResponseEntity<?> getStarRatingCount(
			 	@RequestParam(value = "dashboardId") 
	 				String dashboardId,
	 			@RequestParam(value = "query", required = false, defaultValue = PARAM_DEFAULT_STRING)
	 				String query,
	 			@RequestParam(value = "since", required = false, defaultValue = PARAM_DEFAULT_LONG)
	 				long since,
	 			@RequestParam(value = "sentiment", required = false, defaultValue = PARAM_DEFAULT_STRING)
	 				String sentiment) throws CommonException {
		
		Filters filters = Filters.fromParameters(dashboardId, query, since, sentiment);
		Map<Integer, Long> ratings = feedbackService.getRatingCounts(filters);

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
	public ResponseEntity<?> getAverageRating(
			 	@RequestParam(value = "dashboardId") 
					String dashboardId,
				@RequestParam(value = "query", required = false, defaultValue = PARAM_DEFAULT_STRING)
					String query,
				@RequestParam(value = "since", required = false, defaultValue = PARAM_DEFAULT_LONG)
					long since,
				@RequestParam(value = "sentiment", required = false, defaultValue = PARAM_DEFAULT_STRING)
					String sentiment) throws CommonException {

		Map<String, Double> map = new HashMap<String, Double>();
		Filters filters = Filters.fromParameters(dashboardId, query, since, sentiment);
		map.put("average", feedbackService.getAverageRating(filters, true));

		LoggerUtil.log(Level.INFO, "[Feedback/RatingAverage] Calculated Average");

		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(map));
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize average rating",
									  HttpStatus.NO_CONTENT.value());
		}
	}

	@GetMapping("/rating/negativeperday")
	public ResponseEntity<?> getNegativePerDay(
			 	@RequestParam(value = "dashboardId") 
					String dashboardId,
				@RequestParam(value = "query", required = false, defaultValue = PARAM_DEFAULT_STRING)
					String query,
				@RequestParam(value = "since", required = false, defaultValue = PARAM_DEFAULT_LONG)
					long since,
				@RequestParam(value = "sentiment", required = false, defaultValue = PARAM_DEFAULT_STRING)
					String sentiment) throws CommonException {
		
		Filters filters = Filters.fromParameters(dashboardId, query, since, sentiment);
		Map<String, Object> map = negativePerDayService.findNegativePerDay(filters);
		LoggerUtil.log(Level.INFO, "[Feedback/RatingNegativePerDay] Returned " 
											+ map.size() + " days");

		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(map));
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize negative rating counts",
									HttpStatus.NO_CONTENT.value());
		}
	}

	@GetMapping("/commonphrases")
	public ResponseEntity<?> getCommonPhrases(
			 	@RequestParam(value = "dashboardId") 
					String dashboardId,
				@RequestParam(value = "query", required = false, defaultValue = PARAM_DEFAULT_STRING)
					String query,
				@RequestParam(value = "since", required = false, defaultValue = PARAM_DEFAULT_LONG)
					long since,
				@RequestParam(value = "sentiment", required = false, defaultValue = PARAM_DEFAULT_STRING)
					String sentiment) throws CommonException {

		Filters filters = Filters.fromParameters(dashboardId, query, since, sentiment);
		Map<String, Collection<TermVector>> map
						= feedbackService.getCommonPhrases(filters,
														   COMMON_PHRASES_AMOUNT);
		LoggerUtil.log(Level.INFO,
					   "[Feedback/CommonPhrases] Returned " + map.get("result").size() + " phrases");

		try {
			return ResponseEntity.ok(new ObjectMapper().writeValueAsString(map));
		} catch (JsonProcessingException e) {
			throw new CommonException("Unable to serialize common phrases",
									HttpStatus.NO_CONTENT.value());
		}
	}

	/**
	 * handles any CommonExceptions thrown
	 *
	 * @param ex the exception that was thrown
	 * @return a response entity with the message and HTTP status code
	 */
	@ExceptionHandler(CommonException.class)
	public ResponseEntity<String> exceptionHandlerCommon(CommonException ex) {
		LoggerUtil.logException(ex);
		return new ResponseEntity<String>(ex.getMessage(), 
										  HttpStatus.valueOf(ex.getErrorCode()));
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
