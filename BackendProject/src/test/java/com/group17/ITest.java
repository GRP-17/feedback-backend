package com.group17;

import org.springframework.test.web.servlet.MockMvc;

import com.group17.feedback.FeedbackRepository;
import com.group17.feedback.FeedbackService;
import com.group17.phrase.PhraseService;

public interface ITest {
	
	MockMvc getMockMvc();
	FeedbackRepository getRepository();
	FeedbackService getFeedbackService();
	PhraseService getNGramService();

}
