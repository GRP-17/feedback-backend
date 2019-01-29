package com.group17;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BaseTest implements ITest {
	private static final Sentiment EXPECTED_SENTIMENT = Sentiment.NEUTRAL;
	private static final String TEXT_GOOD_SENTIMENT = "This is good";
	
	@Mock private WatsonGateway mockWatsonGateway;

	@Autowired private MockMvc mockMvc;
	@Autowired private FeedbackRepository repository;
	
	@Before
	public void setup() {
		// Initialise all the mocks
		MockitoAnnotations.initMocks(this);

		// Make any Watson Gateway Request for Sentiments return NEUTRAL
		when(mockWatsonGateway.getSentimentByText(anyString())).thenReturn(Sentiment.NEUTRAL);
	}
	
	@Test
	public void testMockWatsonGateway() {
		//  Ensure that the return value is always NEUTRAL for the Autowired 
		//  gateway instance here, and also the gateway instance within the 
		//  Autowired feedback service.
		assertEquals(EXPECTED_SENTIMENT, 
					 mockWatsonGateway.getSentimentByText(TEXT_GOOD_SENTIMENT));
	}

	@Override
	public MockMvc getMockMvc() {
		return mockMvc;
	}

	@Override
	public FeedbackRepository getRepository() {
		return repository;
	}

	@Override
	public WatsonGateway getMockWatsonGateway() {
		return mockWatsonGateway;
	}

}
