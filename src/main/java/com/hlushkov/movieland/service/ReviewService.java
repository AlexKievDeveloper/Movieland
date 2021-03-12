package com.hlushkov.movieland.service;

import com.hlushkov.movieland.entity.Review;

import java.util.List;

public interface ReviewService {

    void addReview(Review review);

    List<Review> findReviewsByMovieId(int movieId);

    //void updateReviews(int movieId, List<Review> reviews);
}
