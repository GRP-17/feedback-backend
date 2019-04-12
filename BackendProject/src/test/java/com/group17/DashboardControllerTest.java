package com.group17;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DashboardControllerTest extends BaseTest {
    private static final String CREATE_DASHBOARD_NAME = "dashboard's name";

    private static List<String> dashboardsCreated = new ArrayList<String>();

    @Test
    public void testAFindAllDashboards() throws Exception {
        getMockMvc()
                .perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dashboard").isArray());
    }

    @Test
    public void testBCreateDashboardEndpoint() throws Exception {
        String result =
                getMockMvc()
                        .perform(
                                post("/dashboard")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(new String("{\"name\":" + CREATE_DASHBOARD_NAME
                                                +  "\"}")))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.name").value(CREATE_DASHBOARD_NAME))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        dashboardsCreated.add(JsonPath.parse(result).read("$.id"));
    }

    @Test
    public void testCUpdateDashboardEndpoint() throws Exception{
        ResultActions result =
                (ResultActions) getMockMvc()
                        .perform(
                                put("/dashboard")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(new String("{\"name\":" + CREATE_DASHBOARD_NAME
                                                +  "\"}")))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name").value(CREATE_DASHBOARD_NAME))
                        .andReturn()
                        .getResponse();
        dashboardsCreated.add(JsonPath.parse(result).read("$.id"));
    }

    @Test
    public void testDDeleteDashboardEndpoint() throws Exception {
        for (String created : dashboardsCreated) {
            getMockMvc().perform(delete("/dashboard/" + created))
                    .andExpect(status().isNoContent());
        }
    }
}