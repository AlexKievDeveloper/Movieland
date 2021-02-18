package com.hlushkov.movieland.dao;

public interface ReviewDao {

    void addReview(int userId, int movieId, String text);
}
