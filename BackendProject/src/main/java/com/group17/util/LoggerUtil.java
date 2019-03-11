package com.group17.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.group17.Application;

/**
 * The logging utility class.
 */
public class LoggerUtil {
	/** The logger used to write the output logs. <p> log4j is used. */
	private static final Logger logger = LogManager.getLogger(Application.class);
	
	private LoggerUtil() {}
	
	/**
	 * Log an exception with the {@link org.apache.logging.log4j.Level#WARN} 
	 * severity.
	 * 
	 * @param exception the exception to be logged
	 */
	public static void logException(Exception exception) {
		logger.warn("[Exception] " + exception.getMessage());
	}
	
	/**
	 * Log something to log4j.
	 * 
	 * @param level the severity level
	 * @param message the message to be logged
	 */
	public static void log(Level level, String message) {
		logger.log(level, message);
	}
	
}
