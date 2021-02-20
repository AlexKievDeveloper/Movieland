package com.hlushkov.movieland.web.filter;

import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.common.UserHolder;
import com.hlushkov.movieland.entity.User;
import com.hlushkov.movieland.security.SecurityService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext;

@Slf4j
@WebFilter(urlPatterns = {"/api/v1/logout", "/api/v1/review", "/api/v1/genre", "/api/v1/movie", "/api/v1/movie/**"})
public class SecurityFilter extends HttpFilter {
    private SecurityService securityService;

    @Override
    public void init(FilterConfig config) {
        ServletContext servletContext = config.getServletContext();
        securityService = getWebApplicationContext(servletContext).getBean(SecurityService.class);
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String userUUID = request.getHeader("user_uuid");
        if (userUUID != null) {
            Optional<User> userOptional = securityService.getUserByUUID(userUUID);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                UserHolder.setUser(user);
                if (Role.USER.equals(user.getRole()) || Role.ADMIN.equals(user.getRole())) {
                    chain.doFilter(request, response);
                }
            }
        } else {
            log.error("Unauthorized access attempt");
            response.sendRedirect("/api/v1/login");
        }
    }
}
