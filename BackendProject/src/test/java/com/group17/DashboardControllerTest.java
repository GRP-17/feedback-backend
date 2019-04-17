package com.group17;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.group17.dashboard.Dashboard;
import com.jayway.jsonpath.JsonPath;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DashboardControllerTest extends BaseTest {
    private static final String CREATE_DASHBOARD_NAME = "dashboard's name";

    private static List<String> dashboardsCreated = new ArrayList<String>();

    @Test
    public void testAFindAllDashboards() throws Exception {
        getMockMvc()
                .perform(get("/dashboards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dashboard").isArray());
    }

    @Test
    public void testBFindADashboard() throws Exception{
        Dashboard dashboard = new Dashboard();

        getMockMvc()
                .perform(get("/dashboards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) jsonPath("$.id",is(dashboard.getId())));
    }

    @Test
    public void testCCreateDashboardEndpoint() throws Exception {
        String result =
                getMockMvc()
                        .perform(
                                post("/dashboards")
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
    public void testDUpdateDashboardEndpoint() throws Exception{
        ResultActions result =
                (ResultActions) getMockMvc()
                        .perform(
                                put("/dashboards")
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
    public void testEDeleteDashboardEndpoint() throws Exception {
        for (String created : dashboardsCreated) {
            getMockMvc().perform(delete("/dashboards/" + created))
                    .andExpect(status().isNoContent());
        }
    }
}