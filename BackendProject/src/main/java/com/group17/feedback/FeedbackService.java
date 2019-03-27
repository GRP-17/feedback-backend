package com.group17.feedback;

import static com.group17.util.Constants.AVERAGE_RATING_FORMAT;
import static com.group17.util.Constants.FEEDBACK_MAX_RATING;
import static com.group17.util.Constants.FEEDBACK_MIN_RATING;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.group17.exception.CommonException;
import com.group17.ngram.NGramService;
import com.group17.ngram.termvector.TermVector;
import com.group17.tone.Sentiment;
import com.group17.tone.WatsonGateway;
import com.group17.util.LoggerUtil;

/**
 * Defines the service that will handle sending the text to a IBM ToneAnalyser
 * for analysing
 */
@Service
public class FeedbackService {
	@Autowired
    private JpaContext jpaContext;
	/**
	 * holds the instance of the FeedbackRepository which represents the database
	 */
	@Autowired
	private FeedbackRepository feedbackRepo;
	/** holds the instance of the factory which will make the resources */
	@Autowired
	private FeedbackResourceAssembler feedbackAssembler;

	@Autowired
	private WatsonGateway watsonGateway;
	@Autowired
	private NGramService phraseService;

	/**
	 * Get every {@link Resource} in the database.
	 *
	 * @return all of the entries
	 */
	public List<Resource<Feedback>> getAllFeedback() {
		return feedbackRepo.findAll().stream().map(feedbackAssembler::toResource).collect(Collectors.toList());
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

	public List<Resource<Feedback>> getPagedFeedback(int indexFrom, int indexTo) {
		EntityManager entityManager = getEntityManager(Feedback.class);
		
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Feedback> cq = qb.createQuery(Feedback.class);
		cq.from(Feedback.class);
		
		List<Feedback> feedback = entityManager
									.createQuery(cq)
									.setFirstResult(indexFrom)
									.setMaxResults(indexTo - indexFrom)
									.getResultList();

		return feedback.stream().map(feedbackAssembler::toResource).collect(Collectors.toList());
	}

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
	 * Get the total appearances of a given {@link Sentiment} in the JPA
	 * {@link Feedback} repository.
	 * <p>
	 * This is not case sensitive.
	 *
	 * @param sentiment the sentiment search for
	 * @return the total appearances
	 */
	public long getCountBySentiment(String sentiment) {
		return feedbackRepo.countBySentiment(sentiment);
	}

	public Map<Sentiment, Long> getSentimentCounts() {
		Map<Sentiment, Long> counts = new HashMap<Sentiment, Long>();
		for (Sentiment sentiment : Sentiment.values()) {
			counts.put(sentiment, getCountBySentiment(sentiment.toString()));
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
	public long getCountByRating(int rating) {
		return feedbackRepo.countByRating(rating);
	}

	public Map<Integer, Long> getRatingCounts() {
		// Key: the ratings [1..5], Value: The count of this rating
		Map<Integer, Long> ratings = new HashMap<Integer, Long>();

		for (int rating = FEEDBACK_MIN_RATING; rating <= FEEDBACK_MAX_RATING; rating++) {
			ratings.put(rating, getCountByRating(rating));
		}
		return ratings;
	}

	public double getAverageRating(boolean formatted) {
		long total = 0;
		for (int i = FEEDBACK_MIN_RATING; i <= FEEDBACK_MAX_RATING; i++) {
			total += feedbackRepo.countByRating(Integer.valueOf(i)) * i;
		}

		// The unformatted average - it could have many trailing decimal values
		double average = (double) total / (double) feedbackRepo.count();
		if (formatted) {
			return average = Double.valueOf(AVERAGE_RATING_FORMAT.format(average));
		}

		return average;
	}
	
	private Collection<?> getCommonPhrasesIds() {
		Calendar cal = new GregorianCalendar();
	    cal.add(Calendar.MONTH, -1);
	    Date lastMonthDate = cal.getTime();   
	    
	    return getEntityManager(Feedback.class)
		    		.createQuery(
		    				"SELECT f.id from Feedback f WHERE f.created > ?1")
		    		.setParameter(1, lastMonthDate, TemporalType.DATE)
		    		.getResultList();
	}

	public Map<String, Collection<TermVector>> getCommonPhrases(String dashboardId, int amount) {
		LoggerUtil.log(Level.INFO, "Retrieving common phrases");

		Set<String> ids = new HashSet<String>();
		for(Object id : getCommonPhrasesIds()) {
			ids.add((String) id);
		}
		
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
	
	private EntityManager getEntityManager(Class<?> entityClazz) {
		return jpaContext.getEntityManagerByManagedType(entityClazz);
	}

	public long getCount() {
		return feedbackRepo.count();
	}

	public WatsonGateway getWatsonGateway() {
		return watsonGateway;
	}
	
	public void setWatsonGateway(WatsonGateway gateway) {
		this.watsonGateway = gateway;
	}

}
