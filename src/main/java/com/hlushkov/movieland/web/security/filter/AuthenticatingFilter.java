package com.hlushkov.movieland.web.security.filter;

import com.hlushkov.movieland.entity.User;
import com.hlushkov.movieland.security.SecurityService;
import com.hlushkov.movieland.security.util.UserHolder;
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
@WebFilter("/*")
public class AuthenticatingFilter extends HttpFilter {
    private transient SecurityService securityService;

    @Override
    public void init(FilterConfig config) {
        ServletContext servletContext = config.getServletContext();
        securityService = getWebApplicationContext(servletContext).getBean(SecurityService.class);
        log.debug("Initializing Authentication filter");
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            Optional<User> userOptional = securityService.getUserByUUID(request.getHeader("userUUID"));
            userOptional.ifPresent(user -> {
                UserHolder.setUser(user);
                log.info("Successfully finished authentication filtering checking: {}", request.getPathInfo());
            });
            chain.doFilter(request, response);
        } finally {
            UserHolder.removeUser();
        }
    }

}
