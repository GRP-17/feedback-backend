package com.group17;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The actual application
 */
@SpringBootApplication
public class Application implements ApplicationRunner {
	/** the logger used to write the output logs : log4j library used */
	private static final Logger logger = LogManager.getLogger(Application.class);

	/**
	 * The single entry point into the java application
	 * @param args any command line arguments
	 * @throws Exception thrown when SpringApplication fails to run
	 */
	// executes the Spring run() function which will start the server
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	public static Logger getLogger() {
		return logger;
	}

	/**
	 * implements the run method for the ApplicationRunner interface
	 * @param applicationArguments an arguments the application should start with
	 * @throws Exception thrown if the application fails to run/start
	 */
	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		//		logger.debug("Debugging log");
		//		logger.info("Info log");
		//		logger.warn("Warning");
		//		logger.error("Error");
		//		logger.fatal("Fatal");
		logger.info("Application started");
	}
}
