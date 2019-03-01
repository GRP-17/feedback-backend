package com.group17.feedback.ngram;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.Analyze;

@Component
public class SearchboxGateway {
	
	@Autowired
    private JestClient client;
	
	public Set<String> analyse(String text) {
		JsonObject obj = getAnalysis(text);
		Set<String> tokens = new HashSet<String>();
		if(obj == null || !obj.has("tokens")) return tokens;
		
		JsonArray array = obj.get("tokens").getAsJsonArray();
		for(int i = 0; i < array.size(); i ++) {
			JsonElement elem = array.get(i);
			
			String token = elem.getAsJsonObject().get("token")
									.getAsString().replace(" _", "").replace("_ ", "");
			
			// Ensure it's 2/3 words long:
			if(token.replace(" ", "").length() <= token.length() - 1) {
				tokens.add(token);
			}
		}
		return tokens;
	}
	
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
