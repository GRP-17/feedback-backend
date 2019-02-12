package com.group17;

import org.apache.logging.log4j.Level;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.group17.util.LoggerUtil;

/**
 * The actual application
 */
@SpringBootApplication
public class Application implements ApplicationRunner {

	/**
	 * The single entry point into the java application
	 * @param args any command line arguments
	 * @throws Exception thrown when SpringApplication fails to run
	 */
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * implements the run method for the ApplicationRunner interface
	 * @param applicationArguments an arguments the application should start with
	 * @throws Exception thrown if the application fails to run/start
	 */
	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		LoggerUtil.log(Level.INFO, "Application started");
	}
}
