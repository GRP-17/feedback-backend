package com.group17.feedback.tone;

import org.apache.logging.log4j.Level;
import org.springframework.stereotype.Component;

import com.group17.feedback.Feedback;
import com.group17.util.LoggerUtil;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

@Component // for spring bean autowiring (dependency injection)
public class WatsonGateway {
	/** The ToneAnalyzer stores the IBM constants and provides endpoint calls. */
    private static ToneAnalyzer toneAnalyzer;
    
    /** The api key, version and url to use. */
    private String key, version, url;
    
    /**
     * Constructor.
     * 
     * @param key the API key used to connect to the tone analyser
     * @param version the version of the tone analyser to use (a date in string form yyyy-mm-dd)
     * @param url the url to the tone analyser
     */
    public WatsonGateway(String key, String version, String url) {
    	this.key = key;
    	this.version = version;
    	this.url = url;
    }
    
    /**
     * Get the analysed {@link Sentiment} for any String.
     * 
     * @param text the String to analyse
     * @return the deduced Sentiment
     */
    public Sentiment getSentimentByText(String text) {
    	return Sentiment.getByToneAnalysis(analyze(text));
    }
    
    /**
     * Analyse the text within a {@link Feedback} object, and set the
     * {@link Sentiment} within the object.
     * 
     * @param feedback the {@link Feedback} to process
     */
    public void deduceAndSetSentiment(Feedback feedback) { 
    	String text = feedback.getText();
		// Calculate the sentiment
		if(text.length() > 0) {
			Sentiment sentiment = getSentimentByText(feedback.getText());
			feedback.setSentiment(sentiment);
		} else {
			feedback.setSentiment(Sentiment.NEUTRAL);
		}
		
		LoggerUtil.log(Level.INFO, "[Sentiment/Analysis] Deduced & set text"
										+ text + " to " + feedback.getSentiment());
    }
    
    /**
     * Method that will analyze a given String, and produce
     * a {@link ToneAnalysis} instance for the respective text.
     * 
     * @param text the text to analyze. Note: limit of 1000 sentences
     */
    private ToneAnalysis analyze(String text) {
    	if(toneAnalyzer == null) {
            IamOptions options = new IamOptions.Builder().apiKey(key).build();
            toneAnalyzer = new ToneAnalyzer(version, options);
            toneAnalyzer.setEndPoint(url);
    	}
    	
        ToneOptions toneOptions = new ToneOptions.Builder().text(text).build();
        return toneAnalyzer.tone(toneOptions).execute();
    }

}
