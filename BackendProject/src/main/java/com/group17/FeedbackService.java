package com.group17;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;

import com.group17.util.CommonException;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

/**
 * Defines the service that will handle sending the text to a IBM ToneAnalyser for analysing
 */
public class FeedbackService {

	/** holds the instance of the FeedbackRepository which represents the database */
	private final FeedbackRepository repository;
	/** holds the instance of the factory which will make the resources */
	private final FeedbackResourceAssembler assembler;
	/** The ToneAnalyzer stores the IBM constants and provides endpoint calls. */
    private final ToneAnalyzer toneAnalyzer;

    /**
     * constructor
     * @param key the API key used to connect to the tone analyser
     * @param version the version of the tone analyser to use (a date in string form yyyy-mm-dd)
     * @param url the url to the tone analyser
     */
    public FeedbackService(FeedbackRepository repository, 
    					   FeedbackResourceAssembler assembler, 
    					   String key, String version, String url) {
    	this.repository = repository;
    	this.assembler = assembler;
    	
        IamOptions options = new IamOptions.Builder().apiKey(key).build();
        toneAnalyzer = new ToneAnalyzer(version, options);
        toneAnalyzer.setEndPoint(url);
    }
    
    public List<Resource<Feedback>> getAllFeedback() {
    	return repository.findAll().stream().map(assembler::toResource).collect(Collectors.toList());
    }
    
    public Resource<Feedback> getFeedbackById(String id) throws CommonException {		
    	Feedback feedback =
    		repository.findById(id)
				.orElseThrow(
					() ->
					new CommonException(
							"Could not find feedback: " + id, HttpStatus.NOT_FOUND.value()));
    	return assembler.toResource(feedback);
    }
    
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
							}
							return repository.save(feedback);
						 })
					.orElseThrow(
							() ->
							new CommonException(
									"Could not find feedback: " + id, HttpStatus.NOT_FOUND.value()));
    	return assembler.toResource(updatedFeedback);
    }
    
    public Resource<Feedback> saveFeedback(Feedback feedback) {
    	return assembler.toResource(repository.save(feedback));
    }
    
    public void deleteResourceById(String id) {
		repository.deleteById(id);
    }

    /**
     * Analyze a {@link Feedback} and print it to console.
     * 
     * @param feedback what to analyze
     */
    public void analyze(Feedback feedback) {
    	ToneAnalysis toneAnalysis = analyzeText(feedback.getText());
		Application.getLogger().info(toneAnalysis);
    }
    
    /**
     * Method that will analyze a given String, and produce
     * a {@link ToneAnalysis} instance for the respective text.
     * 
     * @param text the text to analyze. Note: limit of 1000 sentences
     */
    private ToneAnalysis analyzeText(String text) {
        ToneOptions toneOptions = new ToneOptions.Builder().text(text).build();
        return toneAnalyzer.tone(toneOptions).execute();
    }
    
    
}
