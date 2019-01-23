package com.group17;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FeedbackTest {
	@InjectMocks FeedbackController feedbackController;
	@Mock WatsonGateway watsonGateway;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testSentiment() {
		when(watsonGateway.getSentimentByText(anyString())).thenReturn(Sentiment.NEUTRAL);
		
		try {
			feedbackController.create(new Feedback(Integer.valueOf(5), "This is a great review"));
		} catch (TransactionSystemException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

}
