package com.hlushkov.movieland.web.controller;

import com.hlushkov.movieland.response.AuthResponse;
import com.hlushkov.movieland.security.SecurityService;
import com.hlushkov.movieland.security.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final SecurityService securityService;

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestParam String email, @RequestParam String password, HttpServletResponse response) {
        Optional<Session> optionalSession = securityService.login(email, password);

        if (optionalSession.isPresent()) {
            AuthResponse authResponse = AuthResponse.builder()
                    .nickname(optionalSession.get().getUser().getNickname())
                    .userUUID(optionalSession.get().getUserUUID())
                    .build();

            Cookie cookie = new Cookie("user_uuid", optionalSession.get().getUserUUID());
            Duration duration = Duration.between(LocalDateTime.now(), optionalSession.get().getExpireDate());
            cookie.setMaxAge((int) duration.getSeconds());
            response.addCookie(cookie);

            return ResponseEntity.status(HttpStatus.OK).body(authResponse);
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("logout")
    public ResponseEntity<Object> logout(@CookieValue(value = "user_uuid") Cookie cookie) {
        if (cookie != null && securityService.removeSession(cookie.getValue())) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }
}
