package com.group17.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.group17.Application;

public class LoggerUtil {
	/** the logger used to write the output logs : log4j library used */
	private static final Logger logger = LogManager.getLogger(Application.class);
	
	private LoggerUtil() {}
	
	public static void logException(Exception exception) {
		logger.catching(exception);
	}
	
	public static void log(Level level, String message) {
		logger.log(level, message);
	}
	
}
