package com.group17.ngram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group17.feedback.Feedback;
import com.group17.ngram.termvector.TermVector;

/**
 * Handles anything Searchbox-related; calling the {@link SearchboxGateway}
 * when necessary.
 */
@Service
public class NGramService {

	@Autowired
	private SearchboxGateway gateway;
	
	/**
	 * Register a {@link Feedback} object being created with Searchbox.
	 * <p>
	 * This will add the text of the {@link Feedback} object to the
	 * Searchbox database for future analysis.
	 * 
	 * @param feedback the {@link Feedback} to add
	 * @return whether it was successful
	 */
	public boolean onFeedbackCreated(Feedback feedback) {
		return gateway.put(feedback.getId(), feedback.getText());
	}
	
	/**
	 * Register a {@link Feedback} object being removed from Searchbox.
	 * <p>
	 * This will remove the text of the {@link Feedback} object from the
	 * Searchbox database.
	 * 
	 * @param feedback the {@link Feedback} to remove
	 * @return whether it was successful
	 */
	public boolean onFeedbackRemoved(String id) {
		return gateway.delete(id);
	}
  
	/**
	 * Using the {@link SearchboxGateway}, make a request to the mtermvectors
	 * endpoint and return the terms along with their frequencies for specified
	 * {@link Feedback ids}.
	 * 
	 * @param ids the {@link Feedback} ids to get the {@link TermVector}s of
	 * @return a map of ids against shared term vectors
	 */
	public Map<String, TermVector> getCommonPhrases(Collection<String> ids) {

		// Create fields for JSON
		ArrayList<String> fields = new ArrayList<>();
		fields.add("text_field");

		// Get the term vectors
		Map<String, TermVector> result = gateway.getMTermVectors(ids, fields);
		return result;
	}
	
	public SearchboxGateway getSearchboxGateway() {
		return gateway;
	}
	
	public void setSearchboxGateway(SearchboxGateway gateway) {
		this.gateway = gateway;
	}
  
}
