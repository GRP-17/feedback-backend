package com.group17.feedback.ngram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group17.util.LoggerUtil;

@Service
public class PhraseService {

	@Autowired private PhraseRepository repository;
	@Autowired private SearchboxGateway gateway;
	
	public Collection<String> createPhrases(long when, String feedbackText) {
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
		return tokens;
	}
	
	public List<Phrase> getMostCommonPhrases(int count, long timePeriod) {
		
		// TODO: Find a better way of doing this
		List<Phrase> phrases = repository.findAll();
		phrases.sort(new Comparator<Phrase>() 
		{

			@Override
			public int compare(Phrase o1, Phrase o2) {
				return o2.getNegativeVolume() - o1.getNegativeVolume();
			}
			
		});
		
		List<Phrase> reduced = new ArrayList<Phrase>();
		for(int i = 0; i < count; i ++) {
			if(i >= phrases.size()) break;
			
			reduced.add(phrases.get(i));
		}
		
		return reduced;
	}
	
	public long getCountByNgram(String ngram) {
		return repository.countByNgram(ngram.toLowerCase());
	}

}
