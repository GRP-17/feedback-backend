package com.group17;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.jayway.jsonpath.JsonPath;

public class FeedbackControllerTest extends BaseTest {
	
	@Test
	public void testFindAllEndpointList() throws Exception {
		List<Feedback> feedbackList = getMockRepository().findAll();

		getMockMvc()
			.perform(get("/feedback"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.feedbackList").isArray())
			.andExpect(jsonPath("$._embedded.feedbackList.length()").value(feedbackList.size()));
	}

	@Test
	public void testFindAllEndpointLinks() throws Exception {
		getMockMvc()
			.perform(get("/feedback"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._links").isMap());
	}
	
	@Test
	public void testRootEndpoint() throws Exception {
		// This should link to the '/feedback' endpoint, but this double checks it
		getMockMvc()
			.perform(get(""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._links").isMap());
	}
	
	@Test
	public void testCountEndpoint() throws Exception {
		long count = getMockRepository().count();
		
		getMockMvc()
			.perform(get("/feedback/count"))
			.andExpect(jsonPath("$.count").value(count));
	}
	
	@Test
	public void testSentimentsCount() throws Exception {
		// Build the JSON string we're expecting, for example:
		// {"POSITIVE":2,"NEUTRAL":8,"NEGATIVE":1}
		FeedbackService feedbackService = getFeedbackService();
		StringBuilder expecting = new StringBuilder();
		expecting.append("{");
		for (int i = 0; i < Sentiment.values().length; i ++) {
			String strSentiment = Sentiment.values()[i].toString();
			
			expecting.append('"').append(strSentiment)
					 .append('"').append(":")
					 .append(feedbackService.getCountBySentiment(strSentiment));
			if(i == Sentiment.values().length - 1) {
				expecting.append("}");
			} else {
				expecting.append(",");
			}
		}

		getMockMvc()
			.perform(get("/feedback/sentiments/count"))
			.andExpect(status().isOk())
			.andExpect(content().json(expecting.toString()));
	}
	
	@Test
	public void testCreateDeleteEndpoint() throws Exception {
		// Call the create endpoint
		String result =
				getMockMvc()
					.perform(
							post("/feedback")
							.contentType(MediaType.APPLICATION_JSON)
							.content(new String("{\"rating\":5, \"text\": \"this is a test\"}")))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.rating").value(5))
					.andExpect(jsonPath("$.text").value("this is a test"))
					.andReturn()
					.getResponse()
					.getContentAsString();

		// Call the delete endpoint to delete the entry we just created
		String testFeedBackId = JsonPath.parse(result).read("$.id");
		getMockMvc()
			.perform(delete("/feedback/" + testFeedBackId))
			.andExpect(status().isNoContent());
	}
	
}
