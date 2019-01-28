package com.group17;

import org.springframework.test.web.servlet.MockMvc;

public interface ITest {
	
	MockMvc getMockMvc();
	FeedbackRepository getRepository();
	WatsonGateway getMockWatsonGateway();
//	FeedbackService getFeedbackService();

}
