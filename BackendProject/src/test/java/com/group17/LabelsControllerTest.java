package com.group17;

import com.group17.label.Label;
import com.jayway.jsonpath.JsonPath;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LabelsControllerTest extends BaseTest {
    private static List<String> labelCreated = new ArrayList<String>();
    private static final String TEST_DASHBOARD_ID = "e99f1a5e-5666-4f35-a08b-190aeeb2d0db";

    @Test
    public void testTFindAllLabels() throws Exception{
        getMockMvc()
                .perform(get("/labels?dashboardId=" + TEST_DASHBOARD_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("._links").isArray());
    }

    @Test
    public void testUFindOneLabel() throws Exception{
        Label label = new Label();
        JsonPathResultMatchers resultActions = jsonPath(".labelId",is(label.getLabelId()));

        getMockMvc()
                .perform(get("/labels?dashboardId=" + TEST_DASHBOARD_ID))
                .andExpect(status().isOk())
                .andExpect(resultActions.exists());
    }

    @Test
    public void testVCreateLabel() throws Exception{
        String result =
                getMockMvc()
                        .perform(
                                post("/labels?dashboardId=" + TEST_DASHBOARD_ID)
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        labelCreated.add(JsonPath.parse(result).read("$.id"));
    }

    @Test
    public void testWUpdateLabel() throws Exception{
        String result = getMockMvc()
                .perform(
                        put("/labels?dashboardId="  + TEST_DASHBOARD_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        labelCreated.add(JsonPath.parse(result).read("$.id"));
    }

    @Test
    public void testXDeleteLabel() throws Exception{
        for (String created : labelCreated) {
            getMockMvc().perform(delete("/labels?dashboardId=" + TEST_DASHBOARD_ID + "/" + created))
                    .andExpect(status().isNoContent());
        }
    }
}
