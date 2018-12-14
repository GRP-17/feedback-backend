package com.group17;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

/**
 * Defines the service that will handle sending the text to a IBM ToneAnalyser for analysing
 */
public class AnalysisService {

    /** The ToneAnalyzer stores the IBM constants and provides endpoint calls. */
    private ToneAnalyzer toneAnalyzer;

    /**
     * constructor
     * @param key the API key used to connect to the tone analyser
     * @param version the version of the tone analyser to use (a date in string form yyyy-mm-dd)
     * @param url the url to the tone analyser
     */
    public   AnalysisService(String key, String version, String url) {
        String IBM_API_KEY = key;
        String IBM_VERSION = version;
        String IBM_URL = url;
        IamOptions options = new IamOptions.Builder().apiKey(IBM_API_KEY).build();
        toneAnalyzer = new ToneAnalyzer(IBM_VERSION, options);
        toneAnalyzer.setEndPoint(IBM_URL);
    }

    /**
     * method which will analyse the text
     * @param text the text to analyse
     *        note: limit of 1000 sentences
     */
    public ToneAnalysis analyze(String text)
    {
        ToneOptions toneOptions = new ToneOptions.Builder().text(text).build();
        return toneAnalyzer.tone(toneOptions).execute();
    }
}
