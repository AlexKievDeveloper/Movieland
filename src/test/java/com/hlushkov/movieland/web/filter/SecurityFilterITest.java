package com.hlushkov.movieland.web.filter;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.User;
import com.hlushkov.movieland.security.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
class SecurityFilterITest {
    private MockMvc mockMvcWithSecurityFilter;
    @Autowired
    private WebApplicationContext context;
    @InjectMocks
    private SecurityFilter securityFilter;
    @Mock
    private SecurityService securityService;

    @BeforeEach
    void setMockMvc() {
        MockitoAnnotations.openMocks(this);
        mockMvcWithSecurityFilter = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(sharedHttpSession())
                .addFilter(securityFilter)
                .build();
    }

    @Test
    @DataSet(provider = TestData.UserProvider.class)
    @DisplayName("Immediately transfers control to the next filter if the current request is in the list of allowed url")
    void doFilterForAllowedUrls() throws Exception {
        //prepare
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", "user@gmail.com");
        userInfo.put("password", "user");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserCredentials = objectMapper.writeValueAsString(userInfo);

        //when+then
        MockHttpServletResponse response = mockMvcWithSecurityFilter.perform(post("/login")
                .content(jsonUserCredentials)
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertNotNull(response.getCookie("user_uuid"));
    }

    @Test
    @DataSet(provider = TestData.UserProvider.class)
    @DisplayName("Return unauthorized status if no cookie in request")
    void doFilterIfNoCookieIsPresent() throws Exception {
        //when+then
        mockMvcWithSecurityFilter.perform(delete("/logout"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse();
    }

    @Test
    @DataSet(provider = TestData.UserProvider.class)
    @DisplayName("Return unauthorized status if no valid cookie in request")
    void doFilterIfNoRequiredCookieIsPresent() throws Exception {
        //prepare
        Cookie cookie = new Cookie("no_valid_cookie", "uuid");
        //when+then
        mockMvcWithSecurityFilter.perform(delete("/logout")
                .cookie(cookie))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse();
    }

    @Test
    @DataSet(provider = TestData.UserProvider.class)
    @DisplayName("Return unauthorized status if no valid cookie in request")
    void doFilterIfRequiredCookieIsPresentWithNoValidToken() throws Exception {
        //prepare
        Cookie cookie = new Cookie("user_uuid", "uuid");
        //when+then
        mockMvcWithSecurityFilter.perform(delete("/logout")
                .cookie(cookie))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse();
    }

    @Test
    @DataSet(provider = TestData.UserProvider.class)
    @DisplayName("Return unauthorized status if no valid cookie in request")
    void doFilterIfRequiredCookieIsPresentWithValidToken() throws Exception {
        //prepare
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", "user@gmail.com");
        userInfo.put("password", "user");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUserCredentials = objectMapper.writeValueAsString(userInfo);

        MockHttpServletResponse response = mockMvcWithSecurityFilter.perform(post("/login")
                .content(jsonUserCredentials)
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        when(securityService.getUserByUUID(any())).thenReturn(Optional.of(User.builder().role(Role.USER).build()));
        //when+then
        mockMvcWithSecurityFilter.perform(delete("/logout")
                .cookie(response.getCookie("user_uuid")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
    }

}