package com.group17;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.jayway.jsonpath.JsonPath;
import org.springframework.http.MediaType;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FeedbackRepository repository;

    static private String testFeedBackId;

    @Test
    public void testAFindAllShouldReturnFeedbackList() throws Exception {
        List<Feedback> feedbackList = repository.findAll();

        this.mockMvc.perform(get("/feedback"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.feedbackList").isArray())
                .andExpect(jsonPath("$._embedded.feedbackList.length()").value(feedbackList.size()));
    }

    @Test
    public void testBFindAllShouldReturnLinks() throws Exception {
        this.mockMvc.perform(get("/feedback"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").isMap());
    }

    @Test
    public void testCCreateShouldReturnResults() throws Exception {

        String result = this.mockMvc.perform(post("/feedback")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new String("{\"rating\":7, \"text\": \"this is a test\"}")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(7))
                .andExpect(jsonPath("$.text").value("this is a test"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        testFeedBackId = JsonPath.parse(result).read("$.id");
    }

    @Test
    public void testDDeleteShouldReturnResults() throws Exception {
        this.mockMvc.perform(delete("/feedback/" + testFeedBackId))
                .andExpect(status().isNoContent());
    }
}