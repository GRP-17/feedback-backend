package com.group17;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.group17.feedback.Feedback;
import com.group17.feedback.FeedbackRepository;
import com.group17.feedback.FeedbackService;
import com.group17.feedback.tone.Sentiment;
import com.group17.mocking.MockSearchboxGateway;
import com.group17.mocking.MockWatsonGateway;
import com.group17.phrase.PhraseService;

import java.util.List;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BaseTest implements ITest {
	private static final Sentiment EXPECTED_SENTIMENT = Sentiment.NEUTRAL;
	private static final String TEXT_GOOD_SENTIMENT   = "This is good";

	private static final Feedback DUMMY_FEEDBACK 	  = new Feedback();
	private static final String DUMMY_FEEDBACK_ID 	  = "dummy id";
	private static final List<String> DUMMY_IDS		  = new ArrayList<String>();
	
	@Autowired private FeedbackService feedbackService;
	@Autowired private PhraseService ngramService;
	
	@Autowired private MockMvc mockMvc;
	@Autowired private FeedbackRepository repository;
	
	@Before
	public void setup() {
		// Initialise all the mocks
		MockitoAnnotations.initMocks(this);

		// Inject mocks
		feedbackService.setWatsonGateway(new MockWatsonGateway());
		ngramService.setSearchboxGateway(new MockSearchboxGateway());
	}
	
	@Test
	public void testMockWatsonGateway() {
		//  Ensure that the return value is always NEUTRAL for the Autowired 
		//  gateway instance here, and also the gateway instance within the 
		//  Autowired feedback service.
		assertEquals(EXPECTED_SENTIMENT, 
					 feedbackService.getWatsonGateway()
					 		.getSentimentByText(TEXT_GOOD_SENTIMENT));
	}
	
	@Test
	public void testMockSearchboxGateway() {
		assertEquals(ngramService.onFeedbackCreated(DUMMY_FEEDBACK), true);
		assertEquals(ngramService.onFeedbackRemoved(DUMMY_FEEDBACK_ID), true);
		assertEquals(ngramService.getCommonPhrases(DUMMY_IDS).isEmpty(), true);
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
	public FeedbackService getFeedbackService() {
		return feedbackService;
	}

	@Override
	public PhraseService getNGramService() {
		// TODO Auto-generated method stub
		return ngramService;
	}

}
