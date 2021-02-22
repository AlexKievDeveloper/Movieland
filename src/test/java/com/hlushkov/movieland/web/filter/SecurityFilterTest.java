package com.hlushkov.movieland.web.filter;

import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.entity.User;
import com.hlushkov.movieland.security.SecurityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestWebContextConfiguration
@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {
    @InjectMocks
    private SecurityFilter securityFilter;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private SecurityService securityService;

    @Test
    @DisplayName("Check methods invoking when request has cookie")
    void doFilterWithCookie() throws IOException, ServletException {
        //prepare
        when(request.getHeader("user_uuid")).thenReturn("uuid");
        when(securityService.getUserByUUID("uuid")).thenReturn(Optional.of(User.builder().role(Role.USER).build()));
        //when
        securityFilter.doFilter(request, response, filterChain);
        //then
        verify(request).getHeader("user_uuid");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Check methods invoking when request hasn`t cookie")
    void doFilterNoValidCookies() throws IOException, ServletException {
        //when
        securityFilter.doFilter(request, response, filterChain);
        //then
        verify(request).getHeader("user_uuid");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}