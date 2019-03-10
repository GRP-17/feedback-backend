package com.group17.feedback.ngram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Map;

@Service
public class PhraseService {

	@Autowired
	private SearchboxGateway gateway;

	// use the gateway to make a request to the mtermvectors endpoint
	// and return the terms and their frequencies
	public Map<String, Integer> getCommonPhrases() {
		// create ids to send
		ArrayList<String> ids = new ArrayList<>();
		ids.add("a");
		ids.add("b");

		// create fields
		ArrayList<String> fields = new ArrayList<>();
		fields.add("text_field");

		Map<String, Integer> result = gateway.getMTermVectors(ids, fields);
		return result;
	}

}
