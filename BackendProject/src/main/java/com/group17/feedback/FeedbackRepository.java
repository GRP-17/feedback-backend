package com.group17.feedback;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group17.tone.Sentiment;

/**
 * Used to represent the database using Jpa
 */
public interface FeedbackRepository extends JpaRepository<Feedback, String> {
	
	/**
	 * Implemented by JPA, that will return the count of the
	 * feedbacks with the matching sentiment field.
	 * <p>
	 * This is not case sensitive.
	 * 
	 * @param sentiment the sentiment to find the count of
	 * @return the total count of the given sentiment
	 * @see Sentiment
	 */
	long countBySentiment(String sentiment);
	
	long countByRating(Integer rating);
	
}