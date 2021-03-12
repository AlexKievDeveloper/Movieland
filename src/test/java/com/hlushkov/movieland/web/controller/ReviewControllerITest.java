package com.hlushkov.movieland.web.controller;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.User;
import com.hlushkov.movieland.security.util.UserHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@ExtendWith(MockitoExtension.class)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReviewControllerITest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setMockMvc() {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(sharedHttpSession()).build();
    }

    @Test
    @DisplayName("Adds review and returns response with status 200")
    @DataSet(provider = TestData.ReviewsProvider.class,
            executeStatementsBefore = "SELECT setval('reviews_review_id_seq', 2)", cleanAfter = true)
    @ExpectedDataSet(provider = TestData.ResultReviewsProvider.class)
    void addReview() throws Exception {
        //prepare
        Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("movieId", 1);
        parametersMap.put("userId", 1);
        parametersMap.put("text", "Nice film!");
        String addReviewJson = objectMapper.writeValueAsString(parametersMap);

        //when+then
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().id(1).role(Role.ADMIN).build());

            mockMvc.perform(post("/reviews")
                    .content(addReviewJson)
                    .contentType("application/json"))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }
    }

}
