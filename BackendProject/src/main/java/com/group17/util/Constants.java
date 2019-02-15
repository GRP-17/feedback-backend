package com.group17.util;

import java.text.DecimalFormat;

public class Constants {
	public static final int FEEDBACK_MIN_RATING = 1;
	public static final int FEEDBACK_MAX_RATING = 5;
	
	public static final DecimalFormat AVERAGE_RATING_FORMAT 
											= new DecimalFormat("#.##");
	
	public static final String DASHBOARD_DEFAULT_ENDPOINTS 
											= "feedback,feedback_count,feedback_rating_average,"
													+ "feedback_rating_count,feedback_sentiment_count,"
													+ "feedback_rating_negative";
	
	private Constants() {}

}
