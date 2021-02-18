package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.dao.jdbc.JdbcReviewDao;
import com.hlushkov.movieland.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultReviewService implements ReviewService {
    private final JdbcReviewDao jdbcReviewDao;

    @Override
    public void addReview(int userId, int movieId, String text) {
        jdbcReviewDao.addReview(userId, movieId, text);
    }
}
