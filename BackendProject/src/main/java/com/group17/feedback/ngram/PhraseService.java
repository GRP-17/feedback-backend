package com.group17.feedback.ngram;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		for(String token : gateway.analyse(feedbackText)) {
			
			Optional<Phrase> optional = repository.findById(token);
			boolean create = !optional.isPresent();
			Phrase phrase = create ? null : optional.get();
			if(create) {
				phrase = new Phrase(token, 0);
			}
			phrase.setNegativeVolume(phrase.getNegativeVolume() + 1);
			
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
