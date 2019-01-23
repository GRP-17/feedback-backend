package com.group17;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class FeedbackTest {
	@Autowired @InjectMocks FeedbackService feedbackService;
	@Mock WatsonGateway watsonGateway;
	@Mock FeedbackRepository feedbackRepository;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testSentiment() {
		when(watsonGateway.getSentimentByText(anyString())).thenReturn(Sentiment.NEUTRAL);
	    when(feedbackRepository.save(any(Feedback.class))).thenReturn(new MockFeedback());
		
		System.out.println("Gateway returned " + watsonGateway.getSentimentByText("This is a good review"));
	}

}
