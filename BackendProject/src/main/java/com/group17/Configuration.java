package com.group17;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.group17.feedback.tone.WatsonGateway;
import com.group17.ngram.SearchboxGateway;

/**
 * The {@link org.springframework.context.annotation.Configuration} 
 * for the entire application.
 * <p>
 * This autonomously instantiates the Spring {@link org.springframework.context.annotation.Bean}s 
 * that are autowired throughout the project.
 * <p>
 * Any bean-creating methods are written as the bean's class name in camel case.
 */
@org.springframework.context.annotation.Configuration
@ComponentScan("com.group17")
public class Configuration {
	
	/**
	 * Creates a new instance of the {@link com.group17.feedback.tone.WatsonGateway}, with the
	 * API keys included.
	 * 
	 * @return the {@link com.group17.feedback.tone.WatsonGateway} instance
	 */
	@Bean
	public WatsonGateway watsonGateway() {
		return new WatsonGateway(
				"t6ayyh6UX9UiRiM-SFCkjSOXHdasKGJbiguzWEvu8yUV",
				"2018-11-27",
				"https://gateway-wdc.watsonplatform.net/tone-analyzer/api");
	}
	
	/**
	 * Creates a new instance of the {@link com.group17.ngram.SearchboxGateway}.
	 * 
	 * @return the new com.group17.ngram.SearchboxGateway instance
	 */
	@Bean
	public SearchboxGateway searchboxGateway() {
		return new SearchboxGateway();
	}

}
