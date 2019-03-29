package com.group17.feedback;

import static com.group17.util.Constants.AVERAGE_RATING_FORMAT;
import static com.group17.util.Constants.FEEDBACK_MAX_RATING;
import static com.group17.util.Constants.FEEDBACK_MIN_RATING;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.group17.exception.CommonException;
import com.group17.feedback.filter.Filters;
import com.group17.feedback.filter.FiltersBuilder;
import com.group17.feedback.filter.query.CountBuilder;
import com.group17.feedback.filter.query.FeedbackBuilder;
import com.group17.feedback.filter.query.FeedbackIdsBuilder;
import com.group17.feedback.filter.query.QueryBuilder;
import com.group17.feedback.filter.query.RatingCountBuilder;
import com.group17.feedback.filter.query.SentimentCountBuilder;
import com.group17.ngram.NGramService;
import com.group17.ngram.termvector.TermVector;
import com.group17.tone.Sentiment;
import com.group17.tone.WatsonGateway;
import com.group17.util.DateUtil;
import com.group17.util.LoggerUtil;

/**
 * Defines the service that will handle sending the text to a IBM ToneAnalyser
 * for analysing
 */
@Service
public class FeedbackService {
	@Autowired private JpaContext jpaContext;
	/** holds the instance of the FeedbackRepository which represents the database */
	@Autowired private FeedbackRepository feedbackRepo;
	/** holds the instance of the factory which will make the resources */
	@Autowired private FeedbackResourceAssembler feedbackAssembler;

	@Autowired private WatsonGateway watsonGateway;
	@Autowired private NGramService phraseService;
	
	/**
	 * Save a {@link Feedback} entry to the database.
	 *
	 * @param feedback what to save
	 * @return the newly saved resource
	 */
	public Resource<Feedback> createFeedback(Feedback feedback) {
		watsonGateway.deduceAndSetSentiment(feedback);
		return feedbackAssembler.toResource(feedbackRepo.save(feedback));
	}

	/**
	 * Update a {@link Feedback} entry in the database.
	 *
	 * @param id          the identifier of the entry to update
	 * @param newFeedback the object containing the data to overwrite with
	 * @return the newly saved resource
	 * @throws CommonException if the id isn't valid (no entry with that given id)
	 */
	public Resource<Feedback> updateFeedback(String id, Feedback newFeedback) throws CommonException {
		Feedback updatedFeedback = feedbackRepo.findById(id).map(feedback -> {
			Integer newRating = newFeedback.getRating();
			String newText = newFeedback.getText();
			if (newRating != null) {
				feedback.setRating(newRating);
			}
			if (newText != null) {
				feedback.setText(newText);

				watsonGateway.deduceAndSetSentiment(feedback);
			}
			return feedbackRepo.save(feedback);
		}).orElseThrow(() -> new CommonException("Could not find feedback: " + id, HttpStatus.NOT_FOUND.value()));
		return feedbackAssembler.toResource(updatedFeedback);
	}

	/**
	 * Delete a {@link Feedback} entry in the database.
	 *
	 * @param id the id of the {@link Feedback to delete}
	 * @throws Exception if the id isn't valid (no entry with that given id)
	 */
	public void deleteFeedbackById(String id) throws Exception {
		feedbackRepo.deleteById(id);
	}

	/**
	 * Get a single {@link Resource} object.
	 *
	 * @param id the identifier of the requested entry
	 * @return the entry
	 * @throws CommonException if the id isn't valid (no entry with that given id)
	 */
	public Resource<Feedback> getFeedbackById(String id) throws CommonException {
		Feedback feedback = feedbackRepo.findById(id)
				.orElseThrow(() -> new CommonException("Could not find feedback: " + id, HttpStatus.NOT_FOUND.value()));
		return feedbackAssembler.toResource(feedback);
	}

	/**
	 * Get every {@link Resource} in the database.
	 *
	 * @return all of the entries
	 */
	public List<Resource<Feedback>> getAllFeedback(String dashboardId) {
		Filters filters = FiltersBuilder
								.newInstance()
								.dashboard(dashboardId)
								.build();
		QueryBuilder builder = new FeedbackBuilder(getFEntityManager(), filters);
		List<Feedback> feedback = builder.build().getResultList();
		return feedback.stream().map(feedbackAssembler::toResource).collect(Collectors.toList());
	}
	
	public List<Resource<Feedback>> getPagedFeedback(String dashboardId, int page, int pageSize) {
		if(page < 0) return new ArrayList<Resource<Feedback>>();
		
		int fromIndex = (page - 1) * pageSize;
		int toIndex = page * pageSize;
		
		List<Resource<Feedback>> feedback = getAllFeedback(dashboardId);
		if(feedback.isEmpty()) return feedback;
		if(fromIndex >= feedback.size()) return new ArrayList<Resource<Feedback>>();
		
		toIndex = Math.min(feedback.size() - 1, toIndex);
		
		return feedback.subList(fromIndex, toIndex);
	}
	
	public long getFeedbackCount(String dashboardId) {
		Filters filters = FiltersBuilder
								.newInstance()
								.dashboard(dashboardId)
								.build();
		QueryBuilder builder = new CountBuilder(getFEntityManager(), filters);
		return ((Number) builder.build().getSingleResult()).longValue();
	}

	/**
	 * Get the total appearances of a given {@link Sentiment} in the JPA
	 * {@link Feedback} repository.
	 * <p>
	 * This is not case sensitive.
	 *
	 * @param sentiment the sentiment search for
	 * @return the total appearances
	 */
	public long getCountBySentiment(String dashboardId, String sentiment) {
		Filters filters = FiltersBuilder
								.newInstance()
								.dashboard(dashboardId)
								.sentiment(Sentiment.valueOf(sentiment.toUpperCase()))
								.build();
		QueryBuilder builder = new SentimentCountBuilder(getFEntityManager(), filters);
		return ((Number) builder.build().getSingleResult()).longValue();
	}

	public Map<Sentiment, Long> getSentimentCounts(String dashboardId) {
		Map<Sentiment, Long> counts = new HashMap<Sentiment, Long>();
		for (Sentiment sentiment : Sentiment.values()) {
			counts.put(sentiment, getCountBySentiment(dashboardId,
													  sentiment.toString()));
		}
		return counts;
	}
	
	/**
	 * Get the total appearances of a given rating in the JPA {@link Feedback}
	 * repository.
	 *
	 * @param rating the rating being searched for
	 * @return total appearances of given rating
	 */
	public long getCountByRating(String dashboardId, int rating) {
		Filters filters = FiltersBuilder
								.newInstance()
								.dashboard(dashboardId)
								.rating(rating)
								.build();
		QueryBuilder builder = new RatingCountBuilder(getFEntityManager(), filters);
		return ((Number) builder.build().getSingleResult()).longValue();
	}

	public Map<Integer, Long> getRatingCounts(String dashboardId) {
		// Key: the ratings [1..5], Value: The count of this rating
		Map<Integer, Long> ratings = new HashMap<Integer, Long>();

		for (int rating = FEEDBACK_MIN_RATING; rating <= FEEDBACK_MAX_RATING; rating++) {
			ratings.put(rating, getCountByRating(dashboardId, rating));
		}
		return ratings;
	}

	public double getAverageRating(String dashboardId, boolean formatted) {
		long total = 0;
		for (int i = FEEDBACK_MIN_RATING; i <= FEEDBACK_MAX_RATING; i++) {
			total += getCountByRating(dashboardId, i) * i;
		}

		// The unformatted average - it could have many trailing decimal values
		double denominator = Math.max(1, (double) getFeedbackCount(dashboardId));
		double average = (double) total / denominator;
		LoggerUtil.log(Level.INFO, "Average after: " + average);
		if (formatted) {
			return average = Double.valueOf(AVERAGE_RATING_FORMAT.format(average));
		}

		return average;
	}
	
	public Map<String, Collection<TermVector>> getCommonPhrases(String dashboardId, int amount) {
		// Get the IDs of the Feedback we're going to send to Elasticsearch using queries
		Filters filters = FiltersBuilder
								.newInstance()
								.dashboard(dashboardId)
								.age(DateUtil.getLastMonth())
								.build();
		QueryBuilder idsBuilder = new FeedbackIdsBuilder(getFEntityManager(), filters);
		List<String> ids = idsBuilder.build().getResultList();
		
		LoggerUtil.log(Level.INFO, "Retrieving common phrases");
		
		Map<String, TermVector> phrases = phraseService.getCommonPhrases(ids);
		List<TermVector> sortedPhrases = new ArrayList<TermVector>(phrases.values());
		Collections.sort(sortedPhrases);

		List<TermVector> toReturn = new ArrayList<TermVector>();
		for(int i = 0; i < Math.min(sortedPhrases.size(), amount); i ++) {
			TermVector vec = sortedPhrases.get(i);
			vec.getTerm().replace("  ", " "); // Remove any double spacing
			toReturn.add(vec);
		}
		
		Map<String, Collection<TermVector>> map = new HashMap<String, Collection<TermVector>>();
		map.put("result", toReturn);
		return map;
	}
	
	private EntityManager getFEntityManager() {
		return jpaContext.getEntityManagerByManagedType(Feedback.class);
	}
	
	public WatsonGateway getWatsonGateway() {
		return watsonGateway;
	}
	
	public void setWatsonGateway(WatsonGateway gateway) {
		this.watsonGateway = gateway;
	}

}
