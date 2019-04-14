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
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.group17.feedback.filter.FilterType;
import com.group17.feedback.filter.Filters;
import com.group17.feedback.filter.FiltersBuilder;
import com.group17.feedback.filter.impl.RatingFilter;
import com.group17.feedback.filter.impl.SentimentFilter;
import com.group17.feedback.filter.query.Queries;
import com.group17.feedback.tone.Sentiment;
import com.group17.feedback.tone.WatsonGateway;
import com.group17.ngram.NGramService;
import com.group17.ngram.termvector.TermVector;
import com.group17.util.DateUtil;
import com.group17.util.LoggerUtil;
import com.group17.util.exception.CommonException;

/**
 * The {@link org.springframework.stereotype.Service} that will handle any retrieval
 * of {@link Feedback} related data.
 */
@Service
public class FeedbackService {
	/** Holds the JpaContext instance for entity managers. */
	@Autowired private JpaContext jpaContext;
	/** Holds the instance of the FeedbackRepository which represents the database. */
	@Autowired private FeedbackRepository feedbackRepo;
	/** Holds the instance of the factory which will make the resources. */
	@Autowired private FeedbackResourceAssembler feedbackAssembler;

	/** Stores the gateway for IBM watson.  */
	@Autowired private WatsonGateway watsonGateway;
	/** Stores the service for finding ngrams & common phrases.  */
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
	 * Get every {@link Resource} in the repository.
	 *
	 * @param filters the {@link Filters} to apply
	 * @return all of the entries
	 */
	public List<Resource<Feedback>> getAllFeedback(Filters filters) {
		Query query = Queries.FEEDBACK.build(getFEntityManager(), filters);
		List<Feedback> feedback = query.getResultList();
		return feedback.stream().map(feedbackAssembler::toResource).collect(Collectors.toList());
	}
	
	/**
	 * Get paged {@link Resource}s in the repository.
	 * 
	 * @param filters the {@link Filters} to apply
	 * @param page the page (starting from 1) to get
	 * @param pageSize the size of each page
	 * @return the entries
	 */
	public List<Resource<Feedback>> getPagedFeedback(Filters filters, int page, int pageSize) {
		if(page < 0) return new ArrayList<Resource<Feedback>>();
		
		int fromIndex = (page - 1) * pageSize;
		int toIndex = page * pageSize;
		
		List<Resource<Feedback>> feedback = getAllFeedback(filters);
		if(feedback.isEmpty()) return feedback;
		if(fromIndex >= feedback.size()) return new ArrayList<Resource<Feedback>>();
		
		toIndex = Math.min(feedback.size(), toIndex); 
		
		LoggerUtil.log(Level.INFO, "Getting paged feedback [" + fromIndex + ", " + toIndex + "]");
		
		return feedback.subList(fromIndex, toIndex);
	}
	
	/**
	 * Get the number of rows in the repository.
	 * 
	 * @param filters the {@link Filters} to apply
	 * @return the row count
	 */
	public long getFeedbackCount(Filters filters) {
		Query query = Queries.COUNT.build(getFEntityManager(), filters);
		return ((Number) query.getSingleResult()).longValue();
	}

	/**
	 * Get the total appearances of a given {@link Sentiment} in the JPA
	 * {@link Feedback} repository.
	 * <p>
	 * The {@link Sentiment} should be included within the {@link Filters}.
	 *
	 * @param filters the {@link Filters} to apply
	 * @return the total appearances of this {@link Sentiment}
	 */
	private long getCountBySentiment(Filters filters) {
		Query query = Queries.SENTIMENT_COUNT.build(getFEntityManager(), filters);
		return ((Number) query.getSingleResult()).longValue();
	}

	public Map<Sentiment, Long> getSentimentCounts(Filters filters) {
		Map<Sentiment, Long> counts = new HashMap<Sentiment, Long>();
		
		Sentiment filteredSentiment = null;
		if(filters.hasFilter(FilterType.SENTIMENT)) {
			filteredSentiment = ((SentimentFilter) filters.getFilter(FilterType.SENTIMENT))
										.getSentiment();
		}
		
		for (Sentiment sentiment : Sentiment.values()) {
			if(filteredSentiment != null) {
				
				if(filteredSentiment.equals(sentiment)) {
					counts.put(sentiment, getCountBySentiment(filters));
				} else {
					counts.put(sentiment, 0L);
				}
				
			} else {
				
				Filters clone = filters.clone();
				clone.addFilter(new SentimentFilter(sentiment));
				counts.put(sentiment, getCountBySentiment(clone));
				
			}
		}
		
		return counts;
	}
	
	/**
	 * Get the total appearances of a given rating in the JPA
	 * {@link Feedback} repository.
	 * <p>
	 * The {rating should be included within the {@link Filters}.
	 *
	 * @param filters the {@link Filters} to apply
	 * @return the total appearances of this rating
	 */
	private long getCountByRating(Filters filters) {
		Query query = Queries.RATING_COUNT.build(getFEntityManager(), filters);
		return ((Number) query.getSingleResult()).longValue();
	}

	/**
	 * Get a {@link java.util.Map} of all the ratings against their number
	 * of appearances within the repository.
	 * 
	 * @param filters the {@link Filters} to apply
	 * @return the resultant Map
	 */
	public Map<Integer, Long> getRatingCounts(Filters filters) {
		// Key: the ratings [1..5]
		// Value: the count of this rating (based on filters)
		Map<Integer, Long> ratings = new HashMap<Integer, Long>();

		int filteredRating = Integer.MIN_VALUE;
		if(filters.hasFilter(FilterType.RATING)) {
			filteredRating = ((RatingFilter) filters.getFilter(FilterType.RATING))
									.getRating();
		}
		
		for (int rating = FEEDBACK_MIN_RATING; rating <= FEEDBACK_MAX_RATING; rating++) {
			
			if(filteredRating != Integer.MIN_VALUE) {
				
				if(filteredRating == rating) {
					ratings.put(rating, getCountByRating(filters));
				} else {
					ratings.put(rating, 0L);
				}
				
			} else {
				
				Filters clone = filters.clone();
				clone.addFilter(new RatingFilter(rating));
				ratings.put(rating, getCountByRating(clone));
				
			}
		}
		
		return ratings;
	}

	/**
	 * Get the average numerical rating for the database.
	 * 
	 * @param filters the {@link Filters} to apply
	 * @param formatted whether to neatly format the decimal
	 * @return the average rating
	 */
	public double getAverageRating(Filters filters, boolean formatted) {
		double total = 0;
		int rowCount = 0;
		for(Entry<Integer, Long> entry : getRatingCounts(filters).entrySet()) {
			rowCount += entry.getValue();
			total += entry.getKey() * entry.getValue();
		}
		
		// The unformatted average - it could have many trailing decimal values
		double denominator = Math.max(1, rowCount);
		double average = total / denominator;
		if (formatted) {
			return average = Double.valueOf(AVERAGE_RATING_FORMAT.format(average));
		}

		return average;
	}
	
	/**
	 * Get the common phrases in the (Searchbox) database.
	 * 
	 * @param filters the {@link Filter}s to apply
	 * @param amount the maximum number of common phrases to retrieve
	 * @return the common phrases against their term vectors
	 */
	public Map<String, Collection<TermVector>> getCommonPhrases(Filters filters, int amount) {
		// Add a filter based on the last month, however if they specify a date to find
		// it by already, then we won't merge
		filters = filters.clone().merge(FiltersBuilder
									.newInstance()
									.age(DateUtil.getLastMonth())
									.build(),
								false);

		// Get the IDs of the Feedback we're going to send to Elasticsearch using queries
		Query query = Queries.FEEDBACK_IDS.build(getFEntityManager(), filters);
		List<String> ids = new ArrayList<String>();
		for(Object obj : query.getResultList()) {
			ids.add((String) obj);
		}
		
		for(String id : ids) {
			LoggerUtil.log(Level.INFO, id);
		}
		
		Map<String, Collection<TermVector>> map = new HashMap<String, Collection<TermVector>>();
		List<TermVector> toReturn = new ArrayList<TermVector>();
		map.put("result", toReturn);
		
		if(!ids.isEmpty()) {
			Map<String, TermVector> phrases = phraseService.getCommonPhrases(ids);
			LoggerUtil.log(Level.INFO, "Size before filtering : " + phrases.size());
			if(phrases != null) {
				
				// filter out single word phrases (by searching for a space in the term)
				Map<String, TermVector> filteredPhrases = new HashMap<String, TermVector>();
				for(Map.Entry<String, TermVector> phrase : phrases.entrySet()) {
					if(phrase.getValue().getTerm().contains(" ")) {
						filteredPhrases.put(phrase.getKey(), phrase.getValue());
					} else {
						LoggerUtil.log(Level.INFO, "filtering out : " + phrase.getKey());
					}
				}
				LoggerUtil.log(Level.INFO, "Size after filtering : " + filteredPhrases.size());

				// sort based on score - get *amount* with highest score
				List<TermVector> sortedPhrases = new ArrayList<TermVector>(filteredPhrases.values());
				Collections.sort(sortedPhrases);

				for(int i = 0; i < Math.min(sortedPhrases.size(), amount); i ++) {
					TermVector vec = sortedPhrases.get(i);
					vec.getTerm().replace("  ", " "); // Remove any double spacing
					toReturn.add(vec);
				}
			}
		}
		return map;
	}
	
	/**
	 * Get the {@link javax.persistence.EntityManager} for the {@link Feedback} entity.
	 * 
	 * @return the {@link javax.persistence.EntityManager}
	 */
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
