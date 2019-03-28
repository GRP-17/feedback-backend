package com.group17;

import static com.group17.util.Constants.FEEDBACK_MAX_RATING;
import static com.group17.util.Constants.FEEDBACK_MIN_RATING;
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

import com.group17.tone.Sentiment;
import com.jayway.jsonpath.JsonPath;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FeedbackControllerTest extends BaseTest {
	private static final String CREATE_FEEDBACK_TEXT = "This is a test.";
	private static final int CREATE_FEEDBACK_RATING  = 1;
	
	/** Stores the last created feedbackId, so that the delete test 
	 *  can use the same one(s) */
	private static List<String> feedbacksCreated = new ArrayList<String>();
	
//	@Test
//	public void testBFindAllEndpoint() throws Exception {
//		// We only need to test for links, as there may not be any
//		// feedback (the list of resources) present, since if the
//		// database is empty then the list will not be returned
//		
//		getMockMvc()
//			.perform(get("/feedback"))
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("$._links").isMap());
//	}
//	
//	@Test
//	public void testCRootEndpoint() throws Exception {
//		// This should link to the '/feedback' endpoint, but this double checks it
//		getMockMvc()
//			.perform(get(""))
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("$._links").isMap());
//	}
//	
//	@Test
//	public void testDCountEndpoint() throws Exception {
//		long count = getRepository().count();
//		
//		getMockMvc()
//			.perform(get("/feedback/count"))
//			.andExpect(jsonPath("$.count").value(count));
//	}
//	
//	@Test
//	public void testESentimentsCount() throws Exception {
//		// Build the JSON string we're expecting, for example:
//		// {"POSITIVE":2,"NEUTRAL":8,"NEGATIVE":1}
//		StringBuilder expecting = new StringBuilder();
//		expecting.append("{");
//		for (int i = 0; i < Sentiment.values().length; i ++) {
//			String strSentiment = Sentiment.values()[i].toString();
//			
//			expecting.append('"').append(strSentiment)
//					 .append('"').append(":")
//					 .append(getRepository()
//							 	.countBySentiment(strSentiment));
//			if(i == Sentiment.values().length - 1) {
//				expecting.append("}");
//			} else {
//				expecting.append(",");
//			}
//		}
//		
//		getMockMvc()
//			.perform(get("/feedback/sentiments/count"))
//			.andExpect(status().isOk())
//			.andExpect(content().json(expecting.toString()));
//	}
//	
//	@Test
//	public void testFCreateEndpoint() throws Exception {
//		String result =
//				getMockMvc()
//					.perform(
//							post("/feedback")
//							.contentType(MediaType.APPLICATION_JSON)
//							.content(new String("{\"rating\":" + CREATE_FEEDBACK_RATING 
//													+ ", \"text\": \"" + CREATE_FEEDBACK_TEXT 
//													+ "\"}")))
//					.andExpect(status().isCreated())
//					.andExpect(jsonPath("$.rating").value(CREATE_FEEDBACK_RATING))
//					.andExpect(jsonPath("$.text").value(CREATE_FEEDBACK_TEXT))
//					.andReturn()
//					.getResponse()
//					.getContentAsString();
//
//		feedbacksCreated.add(JsonPath.parse(result).read("$.id"));
//	}
//	
//	@Test
//	public void testHDeleteEndpoint() throws Exception {
//		for (String created : feedbacksCreated) {
//			getMockMvc().perform(delete("/feedback/" + created))
//				.andExpect(status().isNoContent());
//		}
//	}
//
//    @Test
//    public void testIRatingsCount() throws Exception {
//        // Build the JSON string we're expecting, for example:
//        // {"1":2,"2":0,"3":2,"4":1,"5":7}
//        StringBuilder expected = new StringBuilder();
//        expected.append('{');
//        for (int rating = FEEDBACK_MIN_RATING; rating <= FEEDBACK_MAX_RATING; rating++) {
//            expected.append('"').append(rating).append("\":").append(getRepository().countByRating(rating));
//
//            if(rating < FEEDBACK_MAX_RATING){
//                expected.append(',');
//            }
//        }
//        expected.append('}');
//
//        getMockMvc()
//                .perform(get("/feedback/rating/count"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(expected.toString()));
//
//    }
}
