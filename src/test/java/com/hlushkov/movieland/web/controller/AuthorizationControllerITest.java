package com.hlushkov.movieland.web.controller;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.common.response.AuthResponse;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.User;
import com.hlushkov.movieland.security.SecurityService;
import com.hlushkov.movieland.security.util.UserHolder;
import com.hlushkov.movieland.web.filter.AuthenticatingFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
class AuthorizationControllerITest {
    private MockMvc mockMvc;
    private MockMvc mockMvcWithSecurityFilter;
    @InjectMocks
    private AuthenticatingFilter authenticatingFilter;
    @Mock
    private SecurityService securityService;
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setMockMvc() {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(sharedHttpSession()).build();
        mockMvcWithSecurityFilter = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(sharedHttpSession())
                .addFilter(authenticatingFilter)
                .build();
    }

    @Test
    @DataSet(provider = TestData.UserProvider.class, cleanAfter = true)
    @DisplayName("Returns json with userUUID and nickname")
    void login() throws Exception {
        //prepare
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", "user@gmail.com");
        userInfo.put("password", "user");
        String jsonUserCredentials = objectMapper.writeValueAsString(userInfo);

        //when+then
        MockHttpServletResponse response = mockMvc.perform(post("/login")
                .content(jsonUserCredentials)
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertNotNull(response.getContentAsString());
        assertTrue(response.getContentAsString().contains("userUUID"));
        assertTrue(response.getContentAsString().contains("\"nickname\":\"user\""));
    }

    @Test
    @DataSet(provider = TestData.UserProvider.class, cleanAfter = true)
    @DisplayName("Returns bad request")
    void loginIfEmailOrPasswordIsIncorrect() throws Exception {
        //prepare
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", "nouser@gmail.com");
        userInfo.put("password", "user");
        String jsonUserCredentials = objectMapper.writeValueAsString(userInfo);

        //when+then
        MockHttpServletResponse response = mockMvc.perform(post("/login")
                .content(jsonUserCredentials)
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertTrue(response.getContentAsString().contains("Invalid email or password. Please check your credentials and try again."));
    }

    @Test
    @DataSet(provider = TestData.UserProvider.class, cleanAfter = true)
    @DisplayName("Returns bad request")
    void logout() throws Exception {
        //prepare
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.USER).build());

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

            String content = response.getContentAsString();
            String userUUID = objectMapper.readValue(content, AuthResponse.class).getUserUUID();
            when(securityService.getUserByUUID(any())).thenReturn(Optional.of(User.builder().build()));
            //when+then
            mockMvcWithSecurityFilter.perform(delete("/logout")
                    .content(userUUID))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn().getResponse();
        }
    }

    @Test
    @DataSet(provider = TestData.UserProvider.class, cleanAfter = true)
    @DisplayName("Returns bad request")
    void logoutIfNoValidHeaderPresent() throws Exception {
        //when+then
        mockMvcWithSecurityFilter.perform(delete("/logout"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse();
    }

}