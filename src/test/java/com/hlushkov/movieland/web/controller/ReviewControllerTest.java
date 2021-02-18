package com.hlushkov.movieland.web.controller;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.config.TestConfiguration;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@ExtendWith(MockitoExtension.class)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReviewControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setMockMvc() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(sharedHttpSession()).build();
    }

    @Test
    @DisplayName("Returns response with status 400 bad request")
    @DataSet(provider = TestConfiguration.ReviewsProvider.class)
    void addReviewWhenUserUnauthorized() throws Exception {
        //when+then
        mockMvc.perform(post("/review").param("movieId", "1")
                .param("userId", "1")
                .param("text", "Nice film!"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Saves review and returns response with status 200")
    @DataSet(provider = TestConfiguration.ReviewsProvider.class, executeStatementsBefore = "SELECT setval('reviews_review_id_seq', 2)")
    void addReview() throws Exception {
        //when+then
        MockHttpServletResponse response = mockMvc.perform(post("/login").param("email", "user@gmail.com")
                .param("password", "user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        Cookie cookie = new Cookie("user_uuid", response.getCookie("user_uuid").getValue());

        mockMvc.perform(post("/review")
                .param("movieId", "1")
                .param("userId", "1")
                .param("text", "Nice film!")
                .cookie(cookie))
                .andDo(print())
                .andExpect(status().isOk());
    }
}