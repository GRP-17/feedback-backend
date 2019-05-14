package com.group17;

import com.group17.phrase.blacklist.*;
import com.jayway.jsonpath.JsonPath;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BlacklistedPhrasesControllerTest extends BaseTest{
    private static List<String> phraseCreated = new ArrayList<String>();
    private static final String TEST_DASHBOARD_ID = "e99f1a5e-5666-4f35-a08b-190aeeb2d0db";
    private MediaType contenttype = new MediaType("application", "hal+json", Charset.forName("UTF-8"));

    @Test
    public void testYFindAllBlacklisted() throws Exception{
        getMockMvc()
                .perform(get("/blacklistedphrases?dashboardId=" + TEST_DASHBOARD_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("._links").isArray());
    }

    @Test
    public void testZFindNewBlacklistedPhrase() throws Exception{
        BlacklistedPhrase blacklistedPhrase = new BlacklistedPhrase();
        JsonPathResultMatchers resultActions = jsonPath(".id",is(blacklistedPhrase.getId()));

        getMockMvc()
                .perform(get("/blacklistedphrases?dashboardId=" + TEST_DASHBOARD_ID))
                .andExpect(status().isOk())
                .andExpect(resultActions.exists());
    }

    @Test
    public void testXDeleteBlacklistedPhrase() throws Exception{
        for (String created : phraseCreated) {
            getMockMvc().perform(delete("/blacklistedphrases?dashboardId=" + TEST_DASHBOARD_ID + "/" + created))
                    .andExpect(status().isNoContent());
        }
    }
}
