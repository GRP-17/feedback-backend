package com.group17.mocking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import com.group17.ngram.SearchboxGateway;
import com.group17.ngram.termvector.TermVector;

public class MockSearchboxGateway extends SearchboxGateway {

	public MockSearchboxGateway() {
		super("", "", "");
	}

	@Override
	public boolean put(String id, String text) {
		return true;
	}
	
	@Override
	public boolean delete(String id) {
		return true;
	}
	
	@Override
	public Map<String, TermVector> getMTermVectors(Collection<String> ids,
												   ArrayList<String> fields) {
		return new HashMap<String, TermVector>();
	}
	
}
