package com.group17;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.group17.ngram.SearchboxGateway;
import com.group17.tone.WatsonGateway;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

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
	 * Creates a new instance of the {@link com.group17.tone.WatsonGateway}, with the
	 * API keys included.
	 * 
	 * @return the {@link com.group17.tone.WatsonGateway} instance
	 */
	@Bean
	public WatsonGateway watsonGateway() {
		return new WatsonGateway(
				"t6ayyh6UX9UiRiM-SFCkjSOXHdasKGJbiguzWEvu8yUV",
				"2018-11-27",
				"https://gateway-wdc.watsonplatform.net/tone-analyzer/api");
	}
	
	/**
	 * Creates a new instance of the {@link io.searchbox.client.JestClient}, with the
	 * searchbox elasticsearch URL included.
	 * 
	 * @return the {@link io.searchbox.client.JestClient} instance
	 * @throws Exception if an invalid / non-searchbox URL was included
	 */
	@Bean
    public JestClient jestClient() throws Exception {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
        		.Builder("http://paas:df39a7ec80cb55446068d0e6cfde6680@gloin-eu-west-1.searchly.com")
	                .multiThreaded(true)
	                .build());
        return factory.getObject();
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
