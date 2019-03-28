package com.group17.dashboard;

public enum DashboardEndpointType {
	NAME("name"),
	FEEDBACK("feedback"),
	FEEDBACK_COUNT("feedback_count"),
	FEEDBACK_RATING_AVERAGE("feedback_rating_average"),
	FEEDBACK_RATING_COUNT("feedback_rating_count"),
	FEEDBACK_RATING_NEGATIVE("feedback_rating_negative"),
	FEEDBACK_SENTIMENT_COUNT("feedback_sentiment_count"),
	FEEDBACK_COMMON_PHRASES("feedback_common_phrases");
	
	private String jsonKey;
	
	private DashboardEndpointType(String jsonKey) {
		this.jsonKey = jsonKey;
	}
	
	public String getJsonKey() {
		return jsonKey;
	}

}
