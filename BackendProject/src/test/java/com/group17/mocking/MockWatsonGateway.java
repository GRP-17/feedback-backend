package com.group17.mocking;

import com.group17.feedback.tone.Sentiment;
import com.group17.feedback.tone.WatsonGateway;

public class MockWatsonGateway extends WatsonGateway {

	public MockWatsonGateway() {
		super(null, null, null);
	}
	
	@Override
	public Sentiment getSentimentByText(String text) {
		return Sentiment.NEUTRAL;
	}

}
