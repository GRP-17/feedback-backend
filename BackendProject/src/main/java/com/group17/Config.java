package com.group17;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.group17.feedback.ngram.SearchboxGateway;
import com.group17.tone.WatsonGateway;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

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
	
	@Bean
    public JestClient jestClient() throws Exception {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
        		.Builder("http://paas:df39a7ec80cb55446068d0e6cfde6680@gloin-eu-west-1.searchly.com")
                .multiThreaded(true)
                .build());
        return factory.getObject();
    }
	
	@Bean
	public SearchboxGateway searchboxGateway() {
		return new SearchboxGateway();
	}

}
