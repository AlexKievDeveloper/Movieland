package com.hlushkov.movieland.web.security.interceptor;

import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.entity.User;
import com.hlushkov.movieland.security.annotation.Secured;
import com.hlushkov.movieland.security.util.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Secured annotation = handlerMethod.getMethodAnnotation(Secured.class);
        log.debug("Secure annotation: {}", annotation);

        if (annotation != null) {
            User user = UserHolder.getUser();
            if (user != null) {
                log.debug("User from user holder, nickname: {}, email: {}", user.getNickname(), user.getEmail());
                Role[] roles = annotation.value();

                for (Role role : roles) {
                    log.debug("Annotation role: {}", role);
                    if (role.equals(user.getRole())) {
                        log.debug("User role: {}", user.getRole());
                        return true;
                    }
                }

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
