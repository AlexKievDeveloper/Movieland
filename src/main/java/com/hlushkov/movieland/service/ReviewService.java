package com.hlushkov.movieland.service;

public interface ReviewService {

    void addReview(int userId, int movieId, String text);
}
