package com.group17;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

@RunWith(SpringRunner.class)
//@RunWith(MockitoJUnitRunner.Silent.class)
@AutoConfigureMockMvc
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FeedbackControllerTest {
	private static final Sentiment EXPECTED_SENTIMENT = Sentiment.NEUTRAL;
	
	@Autowired private MockMvc mockMvc;
	@Autowired private FeedbackRepository repository;
	@Autowired @InjectMocks private FeedbackService feedbackService;
	@Mock private WatsonGateway watsonGateway;
	
	/** Stores the last created feedbackId, so that the delete test 
	 *  can use the same one */
	private String testFeedBackId;
	
	@Before
	public void setup() {
		// Initialise all the mocks
		MockitoAnnotations.initMocks(this);

		// Make any Watson Gateway Request for Sentiments return NEUTRAL
		when(watsonGateway.getSentimentByText(anyString())).thenReturn(Sentiment.NEUTRAL);
	}
	
	@Test
	public void testMockWatsonGateway() {
		//  Ensure that the return value is always NEUTRAL for the Autowired 
		//  gateway instance here, and also the gateway instance within the 
		//  Autowired feedback service.
		assertEquals(EXPECTED_SENTIMENT, 
					 watsonGateway.getSentimentByText("This is good"));
		assertEquals(EXPECTED_SENTIMENT, 
					 feedbackService.getSentimentByText("This is good"));
	}

	@Test
	public void testFindAllShouldReturnFeedbackList() throws Exception {
		List<Feedback> feedbackList = repository.findAll();

		mockMvc
			.perform(get("/feedback"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.feedbackList").isArray())
			.andExpect(jsonPath("$._embedded.feedbackList.length()").value(feedbackList.size()));
	}

	@Test
	public void testFindAllShouldReturnLinks() throws Exception {
		mockMvc
			.perform(get("/feedback"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._links").isMap());
	}
	
	@Test
	public void testRootEndpoint() throws Exception {
		// This should link to the '/feedback' endpoint, but this double checks it
		mockMvc
			.perform(get(""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._links").isMap());
	}
	
	@Test
	public void testCreateShouldReturnResults() throws Exception {
		String result =
				mockMvc
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
	public void testDeleteShouldReturnResults() throws Exception {
		mockMvc.perform(delete("/feedback/" + testFeedBackId))
					.andExpect(status().isNoContent());
	}
}
