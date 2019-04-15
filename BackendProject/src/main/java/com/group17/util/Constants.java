package com.group17.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.springframework.web.bind.annotation.ValueConstants;

/**
 * The class holding all of the constants used throughout the backend.
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
	
	/** The default number of common phrases to show. */
	public static final int COMMON_PHRASES_AMOUNT = 10;
	
	/** The default page to show for paged feedback.  */
	public static final int DASHBOARD_FEEDBACK_PAGE 	 = 1;
	/** The default page size to show for paged feedback.  */
	public static final int DASHBOARD_FEEDBACK_PAGE_SIZE = 25;

	/** The value passed in for any optional String endpoint parameter.  */
	public static final String PARAM_DEFAULT_STRING = ValueConstants.DEFAULT_NONE; // This will be read in as null
	/** The value passed in for any optional integer endpoint parameter.  */
	public static final String PARAM_DEFAULT_INTEGER = "-100";
	/** The numerical value of {@link PARAM_DEFAULT_INTEGER} for non-String comparisons.  */
	public static final int PARAM_DEFAULT_INTEGER_VALUE = -100;
	/** The value passed in for any optional long endpoint parameter.  */
	public static final String PARAM_DEFAULT_LONG = "-100";
	/** The numerical value of {@link PARAM_DEFAUL_LONG} for non-String comparisons.  */
	public static final long PARAM_DEFAULT_LONG_VALUE = -100;
	
	private Constants() {}

}
