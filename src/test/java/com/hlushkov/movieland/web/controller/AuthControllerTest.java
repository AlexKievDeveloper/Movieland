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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@ExtendWith(MockitoExtension.class)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setMockMvc() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(sharedHttpSession()).build();
    }

    @Test
    @DataSet(provider = TestConfiguration.UserProvider.class)
    @DisplayName("Returns cookie with token for user")
    void login() throws Exception {
        //prepare
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", "user@gmail.com");
        userInfo.put("password", "user");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserCredentials = objectMapper.writeValueAsString(userInfo);

        //when+then
        MockHttpServletResponse response = mockMvc.perform(post("/login")
                .content(jsonUserCredentials)
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertNotNull(response.getCookie("user_uuid"));
    }

    @Test
    @DataSet(provider = TestConfiguration.UserProvider.class)
    @DisplayName("Returns bad request")
    void loginIfEmailOrPasswordIsIncorrect() throws Exception {
        //prepare
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", "nouser@gmail.com");
        userInfo.put("password", "user");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserCredentials = objectMapper.writeValueAsString(userInfo);

        //when+then
        MockHttpServletResponse response = mockMvc.perform(post("/login")
                .content(jsonUserCredentials)
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertNull(response.getCookie("user_uuid"));
    }

    @Test
    @DataSet(provider = TestConfiguration.UserProvider.class)
    @DisplayName("Returns bad request")
    void logout() throws Exception {
        //prepare
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", "user@gmail.com");
        userInfo.put("password", "user");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserCredentials = objectMapper.writeValueAsString(userInfo);

        MockHttpServletResponse response = mockMvc.perform(post("/login")
                .content(jsonUserCredentials)
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        Cookie cookie = new Cookie("user_uuid", response.getCookie("user_uuid").getValue());

        //when+then
        mockMvc.perform(delete("/logout")
                .cookie(cookie)
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
    }

    @Test
    @DataSet(provider = TestConfiguration.UserProvider.class)
    @DisplayName("Returns bad request")
    void logoutIfNoValidCookiePresent() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete("/logout")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertEquals("Missing cookie 'user_uuid' for method parameter of type Cookie", response.getErrorMessage());
    }
}