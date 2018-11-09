package com.group17;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application { 

	// executes the Spring run() function which will start the server
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
	
}