package com.group17.ngram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group17.feedback.Feedback;
import com.group17.ngram.termvector.TermVector;

@Service
public class NGramService {

	@Autowired
	private SearchboxGateway gateway;
	
	public boolean onFeedbackCreated(Feedback feedback) {
		return gateway.put(feedback.getId(), feedback.getText());
	}
	
	public boolean onFeedbackRemoved(String id) {
		return gateway.delete(id);
	}
  
	// use the gateway to make a request to the mtermvectors endpoint
	// and return the terms and their frequencies
	public Map<String, TermVector> getCommonPhrases(Collection<String> ids) {

		// create fields
		ArrayList<String> fields = new ArrayList<>();
		fields.add("text_field");

		Map<String, TermVector> result = gateway.getMTermVectors(ids, fields);
		return result;
	}
	
	public SearchboxGateway getSearchboxGateway() {
		return gateway;
	}
	
	public void setSearchboxGateway(SearchboxGateway gateway) {
		this.gateway = gateway;
	}
  
}
