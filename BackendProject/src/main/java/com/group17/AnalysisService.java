package com.group17;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

public class AnalysisService {

    /**
     * The ToneAnalyzer stores the IBM constants and provides
     * endpoint calls.
     */
    private ToneAnalyzer toneAnalyzer;

    AnalysisService(String key, String version, String url) {
        String IBM_API_KEY = key;
        String IBM_VERSION = version;
        String IBM_URL = url;
        IamOptions options = new IamOptions.Builder().apiKey(IBM_API_KEY).build();
        toneAnalyzer = new ToneAnalyzer(IBM_VERSION, options);
        toneAnalyzer.setEndPoint(IBM_URL);
    }

    public void analyze(String text)
    {
        ToneOptions toneOptions = new ToneOptions.Builder().text(text).build();
        ToneAnalysis toneAnalysis = toneAnalyzer.tone(toneOptions).execute();
        Application.getLogger().info(toneAnalysis);
    }
}
