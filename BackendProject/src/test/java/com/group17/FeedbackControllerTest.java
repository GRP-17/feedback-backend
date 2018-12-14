package com.group17;

import com.jayway.jsonpath.JsonPath;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FeedbackControllerTest {

	private static String testFeedBackId;
	@Autowired private MockMvc mockMvc;
	@Autowired private FeedbackRepository repository;

	@Test
	public void testAFindAllShouldReturnFeedbackList() throws Exception {
		List<Feedback> feedbackList = repository.findAll();

		this.mockMvc
			.perform(get("/feedback"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.feedbackList").isArray())
			.andExpect(jsonPath("$._embedded.feedbackList.length()").value(feedbackList.size()));
	}

	@Test
	public void testBFindAllShouldReturnLinks() throws Exception {
		this.mockMvc
			.perform(get("/feedback"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._links").isMap());
	}

	@Test
	public void testCCreateShouldReturnResults() throws Exception {

		String result =
				this.mockMvc
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
	public void testDDeleteShouldReturnResults() throws Exception {
		this.mockMvc.perform(delete("/feedback/" + testFeedBackId)).andExpect(status().isNoContent());
	}
}
