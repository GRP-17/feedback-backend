package com.group17.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.group17.Application;
import com.group17.Feedback;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

public class LoggerUtil {
	/** the logger used to write the output logs : log4j library used */
	private static final Logger logger = LogManager.getLogger(Application.class);
	
	private LoggerUtil() {}
	
	public static void logFeedbackFindAll(int foundCount) {
		logger.info("[Feedback/Browse] Found. Object list size: " + foundCount);
	}
	
	public static void logFeedbackFindOne(Feedback feedback) {
		logger.info("[Feedback/Retrieve] Retrieved: " + feedback.getId()
							+ ". Object: " + feedback.toString());
	}
	
	public static void logFeedbackCreate(Feedback feedback) {
		logger.info("[Feedback/Create] Created: " + feedback.getId()
							+ ". Object: " + feedback.toString());
	}
	
	public static void logFeedbackUpdate(Feedback feedback) {
		logger.info("[Feedback/Update] Updated: " + feedback.getId()
							+ ". Object: " + feedback.toString());
	}
	
	public static void logFeedbackDeleted(String id) {
		logger.info("[Feedback/Delete] Deleted: " + id);
	}
	
	public static void logAnalysis(Feedback feedback, ToneAnalysis analysis) {
		logger.info("[Analysis] Analysed " + feedback.getId() + ". Analysis:");
		logger.info(analysis);
	}
	
	public static void logException(Exception exception) {
		logger.warn("[Exception] " + exception.getMessage());
	}
	
	public static Logger getLogger() {
		return logger;
	}
	
}
