package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.entity.Review;

import java.util.List;

public interface ReviewDao {

    void addReview(Review review);

    List<Review> findReviewsByMovieId(int movieId);

    //void removeReviewByMovieId(int movieId);
}
