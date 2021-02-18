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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
        MockHttpServletResponse response = mockMvc.perform(post("/login").param("email", "user@gmail.com")
                .param("password", "user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertNotNull(response.getCookie("user_uuid"));
    }

    @Test
    @DataSet(provider = TestConfiguration.UserProvider.class)
    @DisplayName("Returns bad request")
    void loginIfEmailOrPasswordIsIncorrect() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/login").param("email", "nouser@gmail.com")
                .param("password", "user"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertNull(response.getCookie("user_uuid"));
    }

    @Test
    @DataSet(provider = TestConfiguration.UserProvider.class)
    @DisplayName("Returns bad request")
    void logout() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/login").param("email", "user@gmail.com")
                .param("password", "user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        Cookie cookie = new Cookie("user_uuid", response.getCookie("user_uuid").getValue());

        mockMvc.perform(delete("/logout")
                .cookie(cookie))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
    }

    @Test
    @DataSet(provider = TestConfiguration.UserProvider.class)
    @DisplayName("Returns bad request")
    void logoutIfNoValidCookiePresent() throws Exception {
        mockMvc.perform(delete("/logout"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();
    }
}