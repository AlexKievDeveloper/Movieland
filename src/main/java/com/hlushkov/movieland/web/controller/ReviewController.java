package com.hlushkov.movieland.web.controller;

import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.security.SecurityService;
import com.hlushkov.movieland.security.Session;
import com.hlushkov.movieland.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ReviewController {
    private final SecurityService securityService;
    private final ReviewService reviewService;

    @PostMapping("review")
    public ResponseEntity<Object> addReview(@CookieValue(value = "user_uuid") Cookie cookie, @RequestParam int movieId, @RequestParam String text) {
        if (cookie != null) {
            Optional<Session> optionalSession = securityService.getSession(cookie.getValue());
            if (optionalSession.isPresent()) {
                Session session = optionalSession.get();
                if (Role.USER.equals(session.getUser().getRole())) {
                    reviewService.addReview(session.getUser().getId(), movieId, text);
                    return ResponseEntity.ok().build();
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
