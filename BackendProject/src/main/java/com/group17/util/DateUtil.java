package com.group17.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
	
	private DateUtil() {}
	
    public static long getToday() {
		// Get the time at midnight today
		Calendar date = new GregorianCalendar();
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		
		return date.getTimeInMillis();
    }
    
    public static String format(long date) {
    	return DATE_FORMAT.format(new Date(date));
    }

}
