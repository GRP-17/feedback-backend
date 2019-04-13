package com.group17.feedback.tone;

import java.util.HashMap;
import java.util.Map;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

/**
 * An enum of all the possible Sentiments to be within a 
 * {@link com.group17.feedback.Feedback}'s text.
 */
public enum Sentiment {
	/**
	 * A positive tone.
	 */
	POSITIVE(1),
	/**
	 * A neutral tone.
	 * <p>
	 * This could contain unrecognised or colloquial words, a foreign language
	 * or evenly mixed text.
	 */
	NEUTRAL(0),
	/**
	 * A negative tone.
	 */
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
	
	/**
	 * Constructor for this Sentiment.
	 * 
	 * @param relativeWeight the weight assigned to this Sentiment
	 */
	Sentiment(int relativeWeight) {
		this.relativeWeight = relativeWeight;
	}
	
	/**
	 * Use the analysis to find the relative weights of the tones in the text
	 * and from there deduce the overall Sentiment of a given analysis object.
	 * 
	 * @param analysis the object of what to find the Sentiment of
	 * @return the Sentiment deduced for this analysis object
	 */
	public static Sentiment getByToneAnalysis(ToneAnalysis analysis) {
		if(analysis.getDocumentTone() == null) return NEUTRAL;
		
		double weightSum = 0.0;
		for(ToneScore score : analysis.getDocumentTone().getTones()) {
			String id = score.getToneId();
			
			// There can be language sentiments, so we'll skip those
			// and only check for emotional sentiments
			if(!emotionalSentiments.containsKey(id)) continue;
				
			Sentiment sentiment = emotionalSentiments.get(id);
			weightSum += sentiment.relativeWeight * score.getScore();
		}
		
		if(weightSum > 0.0) return POSITIVE;
		else if(weightSum == 0.0) return NEUTRAL;
		else return NEGATIVE;
	}
	
}
