package com.hlushkov.movieland.web.controller;

import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.common.request.AuthRequest;
import com.hlushkov.movieland.common.response.AuthResponse;
import com.hlushkov.movieland.security.SecurityService;
import com.hlushkov.movieland.security.annotation.Secured;
import com.hlushkov.movieland.security.session.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthorizationController {
    private final SecurityService securityService;

    @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        log.debug("Request for login with email: {} received", authRequest.getEmail());
        Optional<Session> optionalSession = securityService.login(authRequest);

        if (optionalSession.isPresent()) {
            AuthResponse authResponse = AuthResponse.builder()
                    .nickname(optionalSession.get().getUser().getNickname())
                    .userUUID(optionalSession.get().getUserUUID())
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(authResponse);
        }
        return ResponseEntity.badRequest().build();
    }

    @Secured({Role.USER, Role.ADMIN})
    @DeleteMapping("logout")
    public ResponseEntity<Object> logout(@CookieValue(value = "user_uuid") Cookie cookie) {
        log.debug("Request for logout received");
        if (cookie != null && securityService.removeSession(cookie.getValue())) {
            return ResponseEntity.ok().build();
        }
        log.debug("userUUID is not found or expired");
        return ResponseEntity.badRequest().build();
    }
}