package com.group17;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.group17.EndpointController;

public class EndpointControllerTest {
	private EndpointController sut = new EndpointController();
	
	@Test
	public void shouldReturnOkForEmptyText() {
		assertEquals(HttpStatus.OK, sut.validateFeedback(new FeedbackEntity(5, "")).getV());
	}
	
	@Test
	public void shouldReturnOkForRatingValid() {
		assertEquals(HttpStatus.OK, sut.validateFeedback(new FeedbackEntity(9, "Text")).getV());
	}
	
	@Test
	public void shouldReturnErrorForRatingBelow() {
		assertEquals(HttpStatus.BAD_REQUEST, sut.validateFeedback(new FeedbackEntity(0, "Text")).getV());
	}
	
	@Test
	public void shouldReturnErrorForRatingAbove() {
		assertEquals(HttpStatus.BAD_REQUEST, sut.validateFeedback(new FeedbackEntity(11, "Text")).getV());
	}
	
	
}
