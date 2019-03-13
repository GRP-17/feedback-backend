package com.group17.util;

import java.text.DecimalFormat;

/**
 * The class holding all of the constants throughout the application.
 */
public class Constants {
	/** The lowest rating a {@link com.group17.feedback.Feedback} can have. */
	public static final int FEEDBACK_MIN_RATING = 1;
	/** The highest rating a {@link com.group17.feedback.Feedback} can have. */
	public static final int FEEDBACK_MAX_RATING = 5;
	
	/** The {@link java.text.DecimalFormat} of the average rating */
	public static final DecimalFormat AVERAGE_RATING_FORMAT 
											= new DecimalFormat("#.##");
	
	/** The default endpoints to return in the dashboard if none are specified */
	public static final String DASHBOARD_DEFAULT_ENDPOINTS 
											= "feedback,feedback_count,feedback_rating_average,"
													+ "feedback_rating_count,feedback_sentiment_count,"
													+ "feedback_rating_negative,feedback_common_phrases";
	
	public static final int DEFAULT_COMMON_PHRASES_AMOUNT = 10;
	
	private Constants() {}

}
