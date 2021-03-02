package com.hlushkov.movieland.web.controller;

import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.security.util.UserHolder;
import com.hlushkov.movieland.common.request.AddReviewRequest;
import com.hlushkov.movieland.entity.Review;
import com.hlushkov.movieland.security.annotation.Secured;
import com.hlushkov.movieland.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class ReviewController {
    private final ReviewService reviewService;

    @Secured({Role.USER, Role.ADMIN})
    @PostMapping("review")
    @ResponseStatus(HttpStatus.CREATED)
    public void addReview(@RequestBody AddReviewRequest addReviewRequest) {
        log.debug("Request to add a review received");
        Review review = Review.builder()
                .user(UserHolder.getUser())
                .text(addReviewRequest.getText())
                .movieId(addReviewRequest.getMovieId())
                .build();
        reviewService.addReview(review);
    }
}
