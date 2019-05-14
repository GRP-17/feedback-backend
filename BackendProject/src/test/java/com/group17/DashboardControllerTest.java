package com.group17;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.group17.dashboard.Dashboard;
import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DashboardControllerTest extends BaseTest {
    private static final String CREATE_DASHBOARD_NAME = "dashboard's name";
    private static List<String> dashboardsCreated = new ArrayList<String>();

    @Test
    public void testAFindAllDashboards() throws Exception {
        getMockMvc()
                .perform(get("/dashboards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").isMap());
    }

    @Test
    public void testBFindNewDashboard() throws Exception{
        Dashboard dashboard = new Dashboard();
        JsonPathResultMatchers resultActions = jsonPath(".id",is(dashboard.getId()));

        getMockMvc()
                .perform(get("/dashboards"))
                .andExpect(status().isOk())
                .andExpect(resultActions.exists());
    }

    @Test
    public void testEDeleteDashboardEndpoint() throws Exception {
        for (String created : dashboardsCreated) {
            getMockMvc().perform(delete("/dashboards/" + created))
                    .andExpect(status().isNoContent());
        }
    }
}