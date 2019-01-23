package com.group17;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;

import com.group17.util.CommonException;
import com.group17.util.LoggerUtil;

/**
 * Defines the service that will handle sending the text to a IBM ToneAnalyser for analysing
 */
public class FeedbackService {

	/** holds the instance of the FeedbackRepository which represents the database */
	private final FeedbackRepository repository;
	/** holds the instance of the factory which will make the resources */
	private final FeedbackResourceAssembler assembler;
	
	@Autowired private WatsonGateway watsonGateway;

    /**
     * Constructor.
     * 
     * @param repository the database representation
     * @param assembler the resource factory
     */
    public FeedbackService(FeedbackRepository repository, 
    					   FeedbackResourceAssembler assembler) {
    	this.repository = repository;
    	this.assembler = assembler;
    }
    
    /**
     * Get every {@link Resource} in the database.
     * 
     * @return all of the entries
     */
    public List<Resource<Feedback>> getAllFeedback() {
    	return repository.findAll().stream().map(assembler::toResource).collect(Collectors.toList());
    }
    
    /**
     * Get a single {@link Resource} object.
     * 
     * @param id the identifier of the requested entry
     * @return the entry
     * @throws CommonException if the id isn't valid (no entry with that given id)
     */
    public Resource<Feedback> getFeedbackById(String id) throws CommonException {		
    	Feedback feedback =
    		repository.findById(id)
				.orElseThrow(
					() ->
					new CommonException(
							"Could not find feedback: " + id, HttpStatus.NOT_FOUND.value()));
    	return assembler.toResource(feedback);
    }
    
    /**
     * Get the total appearances of a given {@link Sentiment} in the
     * JPA {@link Feedback} repository.
     * <p>
     * This is not case sensitive.
     * 
     * @param sentiment the sentiment search for
     * @return the total appearances
     */
    public long getCountBySentiment(String sentiment) {
    	return repository.countBySentiment(sentiment);
    }
    
    /**
     * Update a {@link Feedback} entry in the database.
     * 
     * @param id the identifier of the entry to update
     * @param newFeedback the object containing the data to overwrite with
     * @return the newly saved resource
     * @throws CommonException if the id isn't valid (no entry with that given id)
     */
    public Resource<Feedback> updateFeedback(String id, Feedback newFeedback) throws CommonException {
		Feedback updatedFeedback =
				repository.findById(id)
					.map(feedback -> {
							Integer newRating = newFeedback.getRating();
							String newText = newFeedback.getText();
							if (newRating != null) {
								feedback.setRating(newRating);
							}
							if (newText != null) {
								feedback.setText(newText);
								
								deduceAndSetSentiment(feedback);
							}
							return repository.save(feedback);
						 })
					.orElseThrow(
							() ->
							new CommonException(
									"Could not find feedback: " + id, HttpStatus.NOT_FOUND.value()));
    	return assembler.toResource(updatedFeedback);
    }
    
    /**
     * Save a {@link Feedback} entry to the database.
     * 
     * @param feedback what to save
     * @return the newly saved resource
     */
    public Resource<Feedback> createFeedback(Feedback feedback) {
    	deduceAndSetSentiment(feedback);
    	return assembler.toResource(repository.save(feedback));
    }
    
    /**
     * Delete a {@link Feedback} entry in the database.
     * 
     * @param id the id of the {@link Feedback to delete}
     * @throws Exception if the id isn't valid (no entry with that given id)
     */
    public void deleteFeedbackById(String id) throws Exception {
		repository.deleteById(id);
    }
    
    private void deduceAndSetSentiment(Feedback feedback) { 
    	String text = feedback.getText();
		// Calculate the sentiment
		if(text.length() > 0) {
			Sentiment sentiment = watsonGateway.getSentimentByText(feedback.getText());
			feedback.setSentiment(sentiment);
			LoggerUtil.logAnalysis(feedback);
		} else {
			feedback.setSentiment(Sentiment.NEUTRAL);
		}
    }
    
}