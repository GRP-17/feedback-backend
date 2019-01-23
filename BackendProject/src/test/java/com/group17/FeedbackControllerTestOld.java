package com.group17;


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
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.Assert.assertEquals;
import com.jayway.jsonpath.JsonPath;
import static org.mockito.Mockito.when;

//@RunWith(SpringRunner.class)
@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FeedbackControllerTestOld {
	public static final Feedback MOCK_FEEDBACK = new MockFeedback();
	
	@Autowired private MockMvc mockMvc;
	@Autowired private FeedbackRepository repository;
	@Autowired @InjectMocks FeedbackService feedbackService;
	@Mock WatsonGateway watsonGateway;
	
	private String testFeedBackId;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		// Make any Watson Gateway Request for Sentiments return NEUTRAL
		when(watsonGateway.getSentimentByText(anyString())).thenReturn(Sentiment.NEUTRAL);
	}
	
	@Test
	public void testMockWatsonGateway() {
		for(int i = 0; i < 10; i ++) { System.out.println("-------------------------------"); }
		assertEquals(Sentiment.NEUTRAL, watsonGateway.getSentimentByText("This is good"));
	}

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
