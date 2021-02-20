package com.hlushkov.movieland.web.controller;

import com.hlushkov.movieland.common.UserHolder;
import com.hlushkov.movieland.common.request.ReviewRequest;
import com.hlushkov.movieland.entity.Review;
import com.hlushkov.movieland.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("review")
    public ResponseEntity<Object> addReview(@RequestBody ReviewRequest reviewRequest) {
        log.debug("Request to add a review received");
        Review review = Review.builder()
                .user(UserHolder.getUser())
                .text(reviewRequest.getText())
                .movieId(reviewRequest.getMovieId())
                .build();
        reviewService.addReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
