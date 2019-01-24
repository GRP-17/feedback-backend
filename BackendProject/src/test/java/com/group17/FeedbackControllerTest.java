package com.group17;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.jayway.jsonpath.JsonPath;

public class FeedbackControllerTest extends BaseTest {
	/** Stores the last created feedbackId, so that the delete test 
	 *  can use the same one */
	private String testFeedBackId;
	
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
	public void testCreateEndpoint() throws Exception {
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

		testFeedBackId = JsonPath.parse(result).read("$.id");
	}
	
	@Test
	public void testDeleteEndpoint() throws Exception {
		getMockMvc().perform(delete("/feedback/" + testFeedBackId))
						.andExpect(status().isNoContent());
	}
}
