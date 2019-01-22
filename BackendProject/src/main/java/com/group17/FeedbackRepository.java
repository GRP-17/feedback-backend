package com.group17;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Used to represent the database using Jpa
 */
public interface FeedbackRepository extends JpaRepository<Feedback, String> {
	
	/**
	 * A function, implemented by API, that will return the count of
	 * any sentiment in the repository.
	 * 
	 * @param sentiment the sentiment to find the count of. This is case sensitive
	 * @return the total count
	 * @see Sentiment
	 */
	long countBySentiment(String sentiment);
	
}
