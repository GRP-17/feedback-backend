package com.group17;

public class MockFeedback extends Feedback {
	
	public MockFeedback() {
		super(5, "This is a mock feedback");
		setSentiment(Sentiment.NEUTRAL);
	}

}
