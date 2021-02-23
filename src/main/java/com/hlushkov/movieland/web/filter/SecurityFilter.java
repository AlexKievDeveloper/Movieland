package com.hlushkov.movieland.web.filter;

import com.hlushkov.movieland.common.UserHolder;
import com.hlushkov.movieland.entity.User;
import com.hlushkov.movieland.security.SecurityService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext;

@Slf4j
@WebFilter("/**")
public class SecurityFilter extends HttpFilter {
    private SecurityService securityService;

    @Override
    public void init(FilterConfig config) {
        ServletContext servletContext = config.getServletContext();
        securityService = getWebApplicationContext(servletContext).getBean(SecurityService.class);
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        List<String> allowedUrls = new ArrayList<>();
        allowedUrls.add("/login");

        if (allowedUrls.contains(request.getPathInfo())) {
            chain.doFilter(request, response);
        } else {
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("user_uuid".equals(cookie.getName())) {
                        Optional<User> userOptional = securityService.getUserByUUID(cookie.getValue());
                        if (userOptional.isPresent()) {
                            User user = userOptional.get();
                            UserHolder.setUser(user);
                            chain.doFilter(request, response);
                        } else {
                            returnUnauthorized(request, response);
                        }
                    } else {
                        returnUnauthorized(request, response);
                    }
                }
            } else {
                returnUnauthorized(request, response);
            }
        }
    }

    void returnUnauthorized(HttpServletRequest request, HttpServletResponse response) {
        log.error("Unauthorized access attempt, servlet path: {}, request path info: {}",
                request.getServletPath(), request.getPathInfo());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}