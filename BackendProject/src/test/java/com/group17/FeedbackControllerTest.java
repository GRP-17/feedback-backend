package com.group17;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.MediaType;

import com.jayway.jsonpath.JsonPath;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FeedbackControllerTest extends BaseTest {
	/** Stores the last created feedbackId, so that the delete test 
	 *  can use the same one(s) */
	private static List<String> feedbacksCreated = new ArrayList<String>();
	
	@Test
	public void testAFindAllEndpointList() throws Exception {
		List<Feedback> feedbackList = getRepository().findAll();

		getMockMvc()
			.perform(get("/feedback"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.feedbackList").isArray())
			.andExpect(jsonPath("$._embedded.feedbackList.length()").value(feedbackList.size()));
	}

	@Test
	public void testBFindAllEndpointLinks() throws Exception {
		getMockMvc()
			.perform(get("/feedback"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._links").isMap());
	}
	
	@Test
	public void testCRootEndpoint() throws Exception {
		// This should link to the '/feedback' endpoint, but this double checks it
		getMockMvc()
			.perform(get(""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._links").isMap());
	}
	
	@Test
	public void testDCountEndpoint() throws Exception {
		long count = getRepository().count();
		
		getMockMvc()
			.perform(get("/feedback/count"))
			.andExpect(jsonPath("$.count").value(count));
	}
	
	@Test
	public void testESentimentsCount() throws Exception {
		// Build the JSON string we're expecting, for example:
		// {"POSITIVE":2,"NEUTRAL":8,"NEGATIVE":1}
		StringBuilder expecting = new StringBuilder();
		expecting.append("{");
		for (int i = 0; i < Sentiment.values().length; i ++) {
			String strSentiment = Sentiment.values()[i].toString();
			
			expecting.append('"').append(strSentiment)
					 .append('"').append(":")
					 .append(getRepository()
							 	.countBySentiment(strSentiment));
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
	public void testFCreateEndpoint() throws Exception {
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

		feedbacksCreated.add(JsonPath.parse(result).read("$.id"));
	}
	
	@Test
	public void testGDeleteEndpoint() throws Exception {
		for (String created : feedbacksCreated) {
			getMockMvc().perform(delete("/feedback/" + created))
				.andExpect(status().isNoContent());
		}
	}
}
