package com.hlushkov.movieland.web.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.hlushkov.movieland.common.request.AddReviewRequest;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
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
class ReviewControllerITest {
    @InjectMocks
    private ReviewController reviewController;
    @Mock
    private AddReviewRequest addReviewRequest;
    @Mock
    private ReviewService reviewService;

    @Test
    @DisplayName("Adds review and returns response with status 200")
    @DataSet(provider = TestData.ReviewsProvider.class,
            executeStatementsBefore = "SELECT setval('reviews_review_id_seq', 2)", cleanAfter = true)
    void addReview() {
        //prepare
        when(addReviewRequest.getMovieId()).thenReturn(1);
        when(addReviewRequest.getText()).thenReturn("Nice film!");

        //when
        reviewController.addReview(addReviewRequest);

        //then
        verify(addReviewRequest).getMovieId();
        verify(addReviewRequest).getText();
        verify(reviewService).addReview(any());
    }

}