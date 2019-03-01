package com.group17;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashSet;

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

import com.group17.feedback.FeedbackRepository;
import com.group17.feedback.ngram.SearchboxGateway;
import com.group17.tone.Sentiment;
import com.group17.tone.WatsonGateway;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BaseTest implements ITest {
	private static final Sentiment EXPECTED_SENTIMENT = Sentiment.NEUTRAL;
	private static final String TEXT_GOOD_SENTIMENT = "This is good";
	
	@Mock private WatsonGateway mockWatsonGateway;
	@Mock private SearchboxGateway searchboxGateway;

	@Autowired private MockMvc mockMvc;
	@Autowired private FeedbackRepository repository;
	
	@Before
	public void setup() {
		// Initialise all the mocks
		MockitoAnnotations.initMocks(this);

		// Make any Watson Gateway Request for Sentiments return NEUTRAL
		when(mockWatsonGateway.getSentimentByText(anyString())).thenReturn(Sentiment.NEUTRAL);
		when(searchboxGateway.analyse(anyString())).thenReturn(new HashSet<String>());
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
