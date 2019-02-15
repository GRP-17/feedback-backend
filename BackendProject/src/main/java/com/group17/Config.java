package com.group17;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.group17.tone.WatsonGateway;

@Configuration
@ComponentScan("com.group17")
public class Config {
	
	@Bean
	public WatsonGateway watsonGateway() {
		return new WatsonGateway(
				"t6ayyh6UX9UiRiM-SFCkjSOXHdasKGJbiguzWEvu8yUV",
				"2018-11-27",
				"https://gateway-wdc.watsonplatform.net/tone-analyzer/api");
	}

}
