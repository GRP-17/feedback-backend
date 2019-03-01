package com.group17.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

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

	public static Date getTodayStart() {
		// Get the time at midnight today by today
		return getDayStart(new Date());
	}
}
