package com.group17.feedback.ngram;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhraseService {

	@Autowired private PhraseRepository repository;
	@Autowired private SearchboxGateway gateway;
	
	public void createPhrases(long when, String feedbackText) {
		ArrayList<Phrase> phrases = new ArrayList<Phrase>();
		for(String token : gateway.analyse(feedbackText)) {
			phrases.add(new Phrase(when, token));
		}
		repository.saveAll(phrases);
	}
	
	public List<Phrase> getMostCommonPhrases(int count, long timePeriod) {
		// TODO - Implement
		if(repository.count() == 0) {
			return new ArrayList<Phrase>();
		}
		return repository.findAll();
	}
	
	public long getCountByNgram(String ngram) {
		return repository.countByNgram(ngram.toLowerCase());
	}

}
