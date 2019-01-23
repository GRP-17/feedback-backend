package com.group17;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class FeedbackTest {
	@InjectMocks FeedbackController feedbackController;
	@Mock WatsonGateway watsonGateway;
	
	@Before
	public void setup() {
		watsonGateway = mock(WatsonGateway.class);
	}
	
	@Test
	public void testSentiment() {
		when(watsonGateway.getSentimentByText(anyString())).thenReturn(Sentiment.NEUTRAL);
		
		System.out.println("Gateway returned " + watsonGateway.getSentimentByText("This is a good review"));
		
		try {
			feedbackController.create(new Feedback(Integer.valueOf(5), "This is a great review"));
		} catch (TransactionSystemException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

}
