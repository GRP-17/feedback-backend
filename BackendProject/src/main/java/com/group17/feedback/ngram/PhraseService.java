package com.group17.feedback.ngram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group17.util.LoggerUtil;

@Service
public class PhraseService {

	@Autowired private PhraseRepository repository;
	@Autowired private SearchboxGateway gateway;
	
	public void createPhrases(long when, String feedbackText) {
		ArrayList<Phrase> phrases = new ArrayList<Phrase>();
		Collection<String> tokens = gateway.analyse(feedbackText);
		LoggerUtil.log(Level.INFO, "[Phrase/Analyze] Loaded " + tokens.size() + " tokens");
		
		for(String token : tokens) {
			token = token.toLowerCase();
			
			if(repository.existsById(token)) {
				// update
				Phrase phrase = repository.getOne(token);
				phrase.setNegativeVolume(phrase.getNegativeVolume() + 1);
				repository.save(phrase);
			} else {
				//create
				Phrase phrase = new Phrase(token, 1);
				repository.save(phrase);
			}
			
			LoggerUtil.log(Level.INFO, "Incremented phrase count for " + token);
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
