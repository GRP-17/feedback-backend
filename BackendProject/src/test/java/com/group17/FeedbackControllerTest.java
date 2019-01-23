package com.group17;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class FeedbackControllerTest {
	private static final Feedback MOCK_FEEDBACK = new MockFeedback();

	@Autowired FeedbackController feedbackController;
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
	public void testFeedbackCreation() {
	    
	    try {
			ResponseEntity<?> responseEntity 
						= feedbackController.create(new Feedback(5, "Test"));
			assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
	    } catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
}
