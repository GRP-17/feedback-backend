package com.group17.feedback.filter;

/**
 * The types of {@link Filter}s that can be applied.
 */
public enum FilterType {
	/** Which {@link Dashboard} do we want to see? */
	DASHBOARD,
	/** When is the earliest date we'd like to see? */
	AGE,
	/** Which {@link Sentiment} would we like to see? */
	SENTIMENT,
	/** Which rating would we like to see? */
	RATING,
	/** Only show if the feedback contains certain text */
	TEXT_CONTAINING,
	/** Only show if the feedback has been assigned certain labels */
	LABEL;
	
}
