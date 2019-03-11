package com.group17.ngram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhraseService {

	@Autowired private SearchboxGateway gateway;
	
}
