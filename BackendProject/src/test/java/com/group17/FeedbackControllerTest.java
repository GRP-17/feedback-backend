package com.group17;

import static com.group17.util.Constants.FEEDBACK_MAX_RATING;
import static com.group17.util.Constants.FEEDBACK_MIN_RATING;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.bind.annotation.ValueConstants.DEFAULT_NONE;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.group17.feedback.Feedback;
import com.group17.feedback.filter.Filters;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.group17.feedback.tone.Sentiment;
import com.jayway.jsonpath.JsonPath;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.bind.annotation.ValueConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FeedbackControllerTest extends BaseTest {
	private static final String CREATE_FEEDBACK_TEXT = "This is a test.";
	private static final int CREATE_FEEDBACK_RATING  = 1;
	private static final String TEST_DASHBOARD_ID = "e99f1a5e-5666-4f35-a08b-190aeeb2d0db";
	private static final long TEST_SINCE = 0;
	private MediaType contenttype = new MediaType("application", "hal+json", Charset.forName("UTF-8"));

	/** Stores the last created feedbackId, so that the delete test
	 *  can use the same one(s) */
	private static List<String> feedbacksCreated = new ArrayList<String>();

	@Test
	public void testFFindAllEndpoint() throws Exception {
		// We only need to test for links, as there may not be any
		// feedback (the list of resources) present, since if the
		// database is empty then the list will not be returned

		getMockMvc()
				.perform(get("/dashboards/" + TEST_DASHBOARD_ID + "/feedback"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links").isMap());
	}

	@Test
	public void testGRootEndpoint() throws Exception {
		// This should link to the '/feedback' endpoint, but this double checks it
		getMockMvc()
				.perform(get(""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links").isMap());
	}

	@Test
	public void testHFindOneEndpoint() throws Exception{
		Feedback feedback = new Feedback();

		getMockMvc()
				.perform(get("/dashboards/" + TEST_DASHBOARD_ID +"/feedback"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contenttype));
	}

	@Test
	public void testICreateFeedbackEndpoint() throws Exception {
		String result =
				getMockMvc()
						.perform(
								post("/dashboards/" + TEST_DASHBOARD_ID +"/feedback")
										.accept(MediaType.APPLICATION_JSON)
										.content(new String("{\"rating\":" + CREATE_FEEDBACK_RATING
												+ ", \"text\": \"" + CREATE_FEEDBACK_TEXT
												+ "\"}")))
						.andExpect(status().isCreated())
						.andExpect(jsonPath("$.rating").value(CREATE_FEEDBACK_RATING))
						.andExpect(jsonPath("$.text").value(CREATE_FEEDBACK_TEXT))
						.andReturn()
						.getResponse()
						.getContentAsString();

		feedbacksCreated.add(JsonPath.parse(result).read("$.id"));
	}

	@Test
	public void testJUpdateFeedbackEndpoint() throws Exception{
		String result = getMockMvc()
				.perform(
						put("/dashboards/" + TEST_DASHBOARD_ID +"/feedback")
								.accept(MediaType.APPLICATION_JSON)
								.content((new String("{\"rating\":" + CREATE_FEEDBACK_RATING
										+ ", \"text\": \"" + CREATE_FEEDBACK_TEXT
										+ "\"}" ))))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		feedbacksCreated.add(JsonPath.parse(result).read("$.id"));
	}

	@Test
	public void testKDeleteEndpoint() throws Exception {
		for (String created : feedbacksCreated) {
			getMockMvc().perform(delete("/feedback?dashboardId=" + TEST_DASHBOARD_ID + "/" + created))
					.andExpect(status().isNoContent());
		}
	}

	@Test
	public void testLStatsEndpoint() throws Exception{
		getMockMvc()
				.perform(get("/feedback/stats?dashboardId=" + TEST_DASHBOARD_ID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contenttype));
	}

	@Test
	public void testNCountEndpoint() throws Exception {
		Filters filters = (Filters) Filters.fromParameters(TEST_DASHBOARD_ID, ValueConstants.DEFAULT_NONE, TEST_SINCE, ValueConstants.DEFAULT_NONE, -100, Collections.singletonList(ValueConstants.DEFAULT_NONE));
		long count = getFeedbackService().getFeedbackCount(filters);

		getMockMvc()
				.perform(get("/feedback/count?dashboardId=" + TEST_DASHBOARD_ID))
				.andExpect(status().isOk())
				.andExpect(content().json(String.valueOf(count)));
	}

	@Test
	public void testOSentimentsCount() throws Exception {
		// Build the JSON string we're expecting, for example:
		// {"POSITIVE":2,"NEUTRAL":8,"NEGATIVE":1}
		StringBuilder expecting = new StringBuilder();
		expecting.append("{");
		for (int i = 0; i < Sentiment.values().length; i ++) {
			Filters filters = Filters.fromParameters(TEST_DASHBOARD_ID, ValueConstants.DEFAULT_NONE, TEST_SINCE, ValueConstants.DEFAULT_NONE, -100, Collections.singletonList(ValueConstants.DEFAULT_NONE));

			expecting.append('"').append(Sentiment.values()[i])
					.append('"').append(":")
					.append(getFeedbackService().getSentimentCounts(filters).get(Sentiment.values()[i]));
			if(i == Sentiment.values().length - 1) {
				expecting.append("}");
			} else {
				expecting.append(",");
			}
		}

		getMockMvc()
				.perform(get("/feedback/sentiments/count?dashboardId=" + TEST_DASHBOARD_ID))
				.andExpect(status().isOk())
				.andExpect(content().json(expecting.toString()));
	}



	@Test
	public void testPStarRatingsCount() throws Exception {
		// Build the JSON string we're expecting, for example:
		// {"1":2,"2":0,"3":2,"4":1,"5":7}
		StringBuilder expected = new StringBuilder();

		expected.append('{');
		for (int rating = FEEDBACK_MIN_RATING; rating <= FEEDBACK_MAX_RATING; rating++) {
			Filters filters = Filters.fromParameters(TEST_DASHBOARD_ID, ValueConstants.DEFAULT_NONE, TEST_SINCE, ValueConstants.DEFAULT_NONE, -100, Collections.singletonList(ValueConstants.DEFAULT_NONE));
			expected.append('"').append(rating).append("\":").append(getFeedbackService().getRatingCounts(filters).get(rating));

			if(rating < FEEDBACK_MAX_RATING){
				expected.append(',');
			}
		}
		expected.append('}');

		getMockMvc()
				.perform(get("/feedback/rating/count?dashboardId=" + TEST_DASHBOARD_ID))
				.andExpect(status().isOk())
				.andExpect(content().json(expected.toString()));

	}

	@Test
	public void testQAverageRatingsCount() throws Exception {
	    // Build the JSON string we're expecting, for example:
        // {"average":3.26}
	    StringBuilder expected = new StringBuilder();
	    expected.append("{\"average\":");

        Filters filters = Filters.fromParameters(TEST_DASHBOARD_ID, ValueConstants.DEFAULT_NONE, TEST_SINCE, ValueConstants.DEFAULT_NONE, -100, Collections.singletonList(ValueConstants.DEFAULT_NONE));
        expected.append(getFeedbackService().getAverageRating(filters, true));
        expected.append('}');

		getMockMvc()
				.perform(get("/feedback/rating/average?dashboardId=" + TEST_DASHBOARD_ID))
				.andExpect(status().isOk())
				.andExpect(content().json(expected.toString()));

	}

	@Test
	public void testRNegativePerDay() throws Exception {
		getMockMvc()
				.perform(get("/feedback/rating/negativeperday?dashboardId=" + TEST_DASHBOARD_ID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contenttype));
	}

	@Test
	public void testSCommonPhrases() throws Exception {
		getMockMvc()
				.perform(get("/feedback/commonphrases?dashboardId=" + TEST_DASHBOARD_ID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contenttype));
	}
}