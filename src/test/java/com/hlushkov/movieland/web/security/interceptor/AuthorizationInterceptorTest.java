package com.hlushkov.movieland.web.security.interceptor;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.security.util.UserHolder;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.User;
import com.hlushkov.movieland.web.controller.AuthorizationController;
import com.hlushkov.movieland.web.controller.MovieController;
import com.hlushkov.movieland.web.security.interceptor.AuthorizationInterceptor;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@ExtendWith(MockitoExtension.class)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthorizationInterceptorTest {
    private MockMvc mockMvcWithMovieController;
    private MockMvc mockMvcWithAuthController;
    @Autowired
    private AuthorizationController authController;
    @Autowired
    private MovieController movieController;

    @BeforeEach
    void setMockMvc() {
        MockitoAnnotations.openMocks(this);
        mockMvcWithMovieController = MockMvcBuilders.standaloneSetup(movieController).addInterceptors(new AuthorizationInterceptor()).build();
        mockMvcWithAuthController = MockMvcBuilders.standaloneSetup(authController).addInterceptors(new AuthorizationInterceptor()).build();
    }

    @Test
    @DataSet(provider = TestData.MoviesCountriesGenresReviewsUsers.class, cleanAfter = true)
    @DisplayName("Check user role")
    void preHandle() throws Exception {
        //when+then
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.USER).build());

            MockHttpServletResponse response = mockMvcWithMovieController.perform(get("/movies"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn().getResponse();

            assertEquals("application/json", response.getContentType());
        }
    }

    @Test
    @DataSet(provider = TestData.UserProvider.class, cleanAfter = true)
    @DisplayName("Return true without checking user role")
    void preHandleMethodWithoutSecureAnnotation() throws Exception {
        //prepare
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", "user@gmail.com");
        userInfo.put("password", "user");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserCredentials = objectMapper.writeValueAsString(userInfo);

        //when+then
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.USER).build());

            MockHttpServletResponse response = mockMvcWithAuthController.perform(post("/login")
                    .content(jsonUserCredentials)
                    .contentType("application/json"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn().getResponse();

            assertEquals("application/json", response.getContentType());
        }
    }
}

