package com.group17;

import org.springframework.test.web.servlet.MockMvc;

import com.group17.feedback.FeedbackRepository;
import com.group17.tone.WatsonGateway;

public interface ITest {
	
	MockMvc getMockMvc();
	FeedbackRepository getRepository();
	WatsonGateway getMockWatsonGateway();
//	FeedbackService getFeedbackService();

}
