package com.group17.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.springframework.web.bind.annotation.ValueConstants;

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
											= new DecimalFormat("#.##", 
																new DecimalFormatSymbols(Locale.US));
	
	public static final int COMMON_PHRASES_AMOUNT = 10;
	
	public static final int DASHBOARD_FEEDBACK_PAGE 	 = 1;
	public static final int DASHBOARD_FEEDBACK_PAGE_SIZE = 25;
	
	public static final String PARAM_DEFAULT_STRING = ValueConstants.DEFAULT_NONE; // This will be read in as null
	public static final long PARAM_DEFAULT_LONG_VALUE = -100;
	public static final String PARAM_DEFAULT_LONG = "-100";
	
	private Constants() {}

}
