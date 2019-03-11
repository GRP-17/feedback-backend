package com.group17.ngram;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.Analyze;

@Component
public class SearchboxGateway {
	
	@Autowired
    private JestClient client;
	
	private JsonObject getAnalysis(String text) {
		Analyze analyze = getAnalyze(text);
		try {
			JestResult result = client.execute(analyze);
			return result.getJsonObject();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Analyze getAnalyze(String text) {
		Analyze analyze = new Analyze.Builder()
				.analyzer("evolutionAnalyzer")
				.index("test").text(text).build();
		return analyze;
	}

}
