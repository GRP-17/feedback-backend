package com.group17;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DashboardControllerTest extends BaseTest {
	
	@Test
	public void testAFeedback() throws Exception {
		getMockMvc()
			.perform(get("/dashboard"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.feedback").isArray());
	}
	
	@Test
	public void testBAverageRating() throws Exception {
		getMockMvc()
			.perform(get("/dashboard"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.feedback_rating_average").exists());
	}
	
}
