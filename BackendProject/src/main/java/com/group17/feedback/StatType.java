package com.group17.feedback;

/**
 * Represents a feedback endpoint for any given {@link Dashboard}.
 */
public enum StatType {
	/** The name of the {@link Dashboard}. */
	DASHBOARD_NAME("dashboard_name"),
	/** The set of all {@link Label}s for this dashboard */
	DASHBOARD_LABELS("dashboard_labels"),
	/** The paged {@link Feedback}. */
	FEEDBACK_PAGED("feedback_paged"),
	/** The {@link Feedback} count. */
	FEEDBACK_COUNT("feedback_count"),
	/** The {@link Feedback} average rating. */
	FEEDBACK_RATING_AVERAGE("feedback_rating_average"),
	/** The {@link Feedback} rating counts for [1..5]. */
	FEEDBACK_RATING_COUNT("feedback_rating_count"),
	/** The daily counts of negative {@link Feedback} left. */
	FEEDBACK_RATING_NEGATIVE("feedback_rating_negative"),
	/** The {@link Feedback} sentiment counts. */
	FEEDBACK_SENTIMENT_COUNT("feedback_sentiment_count"),
	/** The {@link Feedback} sentiment common phrases. */
	FEEDBACK_COMMON_PHRASES("feedback_common_phrases");
	
	/** The key to set the value as in JSON. */
	private String jsonKey;
	
	private StatType(String jsonKey) {
		this.jsonKey = jsonKey;
	}
	
	public String getJsonKey() {
		return jsonKey;
	}

}
