package com.group17.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The time utility class.
 */
public class DateUtil {
	
	private DateUtil() {}
	
	/**
	 * Get the time at 00:00 (midnight) on any day.
	 * 
	 * @param date the date of the day to convert to midnight
	 * @return midnight of the specified day / {@link java.util.Date}
	 */
	public static Date getDayStart(Date date) {
		// Get the time at midnight today by a given date
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * Get the time at 00:00 (midnight) today.
	 * 
	 * @return the {@link java.util.Date} object for at 00:00 today
	 */
	public static Date getTodayStart() {
		return getDayStart(new Date());
	}
	
	public static Date getLastMonth() {
		Calendar cal = new GregorianCalendar();
	    cal.add(Calendar.MONTH, -1);
	    return cal.getTime(); 
	}
	
}
