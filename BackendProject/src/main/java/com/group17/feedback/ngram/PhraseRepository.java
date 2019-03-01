package com.group17.feedback.ngram;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PhraseRepository extends JpaRepository<Phrase, String> {
	
	long countByNgram(String ngram);

}
