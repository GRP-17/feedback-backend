package com.group17;

import com.group17.phrase.blacklist;
import com.jayway.jsonpath.JsonPath;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BlacklistedPhrasesControllerTest extends BaseTest{
    private static List<String> phraseCreated = new ArrayList<String>();
    private static final String TEST_DASHBOARD_ID = "e99f1a5e-5666-4f35-a08b-190aeeb2d0db";

    @Test
    public void testYFindAllBlacklisted() throws Exception{
        getMockMvc()
                .perform(get("/blacklistedphrases?dashboardId" + TEST_DASHBOARD_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath(".$labels").isArray());
    }

    @Test
    public void testUFindOneLabel() throws Exception{
        BlacklistedPhrase blacklistedPhrase = new BlacklistedPhrase();

        getMockMvc()
                .perform(get("/blacklistedphrases?dashboardId" + TEST_DASHBOARD_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) jsonPath("$.id",is(blacklistedPhrase.getId())));
    }

    @Test
    public void testVCreateLabel() throws Exception{
        String result =
                getMockMvc()
                        .perform(
                                post("/blacklistedphrases?dashboardId" + TEST_DASHBOARD_ID)
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        phraseCreated.add(JsonPath.parse(result).read("$.id"));
    }

    @Test
    public void testWUpdateLabel() throws Exception{
        String result = getMockMvc()
                .perform(
                        put("/blacklistedphrases?dashboardId")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        phraseCreated.add(JsonPath.parse(result).read("$.id"));
    }

    @Test
    public void testXDeleteLabel() throws Exception{
        for (String created : phraseCreated) {
            getMockMvc().perform(delete("/blacklistedphrases?dashboardId=" + TEST_DASHBOARD_ID + "/" + created))
                    .andExpect(status().isNoContent());
        }
    }
}
