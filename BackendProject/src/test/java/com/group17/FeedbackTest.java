package com.group17;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(MockitoJUnitRunner.Silent.class)
public class FeedbackTest {
	public static final Feedback MOCK_FEEDBACK = new MockFeedback();
	
	@Autowired @InjectMocks FeedbackService feedbackService;
	
	@Mock WatsonGateway watsonGateway;
	@Mock FeedbackRepository feedbackRepository;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		// Make any Watson Gateway Request for Sentiments return NEUTRAL
		when(watsonGateway.getSentimentByText(anyString())).thenReturn(Sentiment.NEUTRAL);
		// Cancel any Feedback saving & return MOCK_FEEDBACK
	    when(feedbackRepository.save(any(Feedback.class))).thenReturn(MOCK_FEEDBACK);
	}
	
	@Test
	public void testMockWatsonGateway() {
		assertEquals(Sentiment.NEUTRAL, watsonGateway.getSentimentByText("This is good"));
	}
	
	@Test
	public void testFeedbackSaving() {
		long countBefore = feedbackRepository.count();
		feedbackRepository.save(new Feedback(5, "Test"));
		long countAfter = feedbackRepository.count();
		assertEquals(countBefore, countAfter);
	}

//	@Test
//	public void testFeedbackCreation() {
//	    
//	    try {
//			ResponseEntity<?> responseEntity 
//						= feedbackController.create(new Feedback(5, "Test"));
//			assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
//	    } catch (Exception e) {
//			e.printStackTrace();
//			fail();
//		}
//	}
	
}
