package com.group17;

import java.util.HashMap;
import java.util.Map;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.SentenceAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

public enum Sentiment {
	POSITIVE(1),
	NEUTRAL(0),
	NEGATIVE(-1);
	
	/**
	 * A mapping between emotional sentiment ids, for example 'joy',
	 * 'disgust', 'anger', 'sadness' and 'fear' (as per the 
	 * {@link ToneScore} class documentation) and their respective
	 * Sentiment.
	 * 
	 * @see com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore
	 */
	private final static Map<String, Sentiment> emotionalSentiments
				= new HashMap<String, Sentiment>() 
				{{
					put("joy", POSITIVE);
					
					put("disgust", NEGATIVE);
					put("anger", NEGATIVE);
					put("sadness", NEGATIVE);
					put("fear", NEGATIVE);
				}};
				
	/** The relative weight of this Sentiment [-1 .. 1]. */
	private int relativeWeight;	
	
	private Sentiment(int relativeWeight) {
		this.relativeWeight = relativeWeight;
	}
	
	public static Sentiment getByToneAnalysis(ToneAnalysis analysis) {
		if(analysis.getSentencesTone() == null) return NEUTRAL;
		
		double weightSum = 0.0;
		for(SentenceAnalysis sentence : analysis.getSentencesTone()) {
			weightSum += getWeight(sentence);
		}
		
		if(weightSum > 0.0) return POSITIVE;
		else if(weightSum == 0.0) return NEUTRAL;
		else return NEGATIVE;
	}
	
	private static double getWeight(SentenceAnalysis sentence) {
		double weight = 0.0;
		for(ToneScore score : sentence.getTones()) {
			String id = score.getToneId();
			
			// There can be language sentiments, so we'll skip those
			// and only check for emotional sentiments
			if(!emotionalSentiments.containsKey(id)) continue;
				
			Sentiment sentiment = emotionalSentiments.get(id);
			weight += sentiment.relativeWeight * score.getScore();
		}
		return weight;
	}

}
