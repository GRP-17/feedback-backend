package com.group17;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

public class WatsonGateway {
	/** The ToneAnalyzer stores the IBM constants and provides endpoint calls. */
    private final ToneAnalyzer toneAnalyzer;
    
    public WatsonGateway(String key, String version, String url) {
        IamOptions options = new IamOptions.Builder().apiKey(key).build();
        toneAnalyzer = new ToneAnalyzer(version, options);
        toneAnalyzer.setEndPoint(url);
    }
    
    /**
     * Method that will analyze a given String, and produce
     * a {@link ToneAnalysis} instance for the respective text.
     * 
     * @param text the text to analyze. Note: limit of 1000 sentences
     */
    private ToneAnalysis analyze(String text) {
        ToneOptions toneOptions = new ToneOptions.Builder().text(text).build();
        return toneAnalyzer.tone(toneOptions).execute();
    }
    
    public Sentiment getSentimentByText(String text) {
    	return Sentiment.getByToneAnalysis(analyze(text));
    }

}
