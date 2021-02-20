package com.hlushkov.movieland.web.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.hlushkov.movieland.common.request.ReviewRequest;
import com.hlushkov.movieland.config.TestConfiguration;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestWebContextConfiguration
class ReviewControllerTest {
    @InjectMocks
    private ReviewController reviewController;
    @Mock
    private ReviewRequest reviewRequest;
    @Mock
    private ReviewService reviewService;


    @Test
    @DisplayName("Adds review and returns response with status 200")
    @DataSet(provider = TestConfiguration.ReviewsProvider.class, executeStatementsBefore = "SELECT setval('reviews_review_id_seq', 2)")
    void addReview() {
        //prepare
        when(reviewRequest.getMovieId()).thenReturn(1);
        when(reviewRequest.getText()).thenReturn("Nice film!");

        //when
        reviewController.addReview(reviewRequest);

        //then
        verify(reviewRequest).getMovieId();
        verify(reviewRequest).getText();
        verify(reviewService).addReview(any());
    }

}