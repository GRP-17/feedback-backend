package com.group17;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.group17.EndpointController;;

public class EndpointControllerTest {
	private EndpointController sut = new EndpointController();
	
	@Test
	public void shouldReturnErrorForEmptyText() {
		assertEquals(HttpStatus.OK, sut.getResponse(new Feedback(5, "")).getV());
	}
	
	@Test
	public void shouldReturnErrorForRatingValid() {
		assertEquals(HttpStatus.OK, sut.getResponse(new Feedback(9, "Text")).getV());
	}
	
	@Test
	public void shouldReturnErrorForRatingBelow() {
		assertEquals(HttpStatus.BAD_REQUEST, sut.getResponse(new Feedback(0, "Text")).getV());
	}
	
	@Test
	public void shouldReturnErrorForRatingAbove() {
		assertEquals(HttpStatus.BAD_REQUEST, sut.getResponse(new Feedback(11, "Text")).getV());
	}
	
	
}
