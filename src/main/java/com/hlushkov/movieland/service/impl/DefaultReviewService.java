package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.dao.ReviewDao;
import com.hlushkov.movieland.entity.Review;
import com.hlushkov.movieland.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultReviewService implements ReviewService {
    private final ReviewDao reviewDao;

    @Override
    public void addReview(Review review) {
        reviewDao.addReview(review);
    }

    @Override
    public List<Review> findReviewsByMovieId(int movieId) {
        log.info("Method findReviewsByMovieId started: {}", LocalDateTime.now());
        return reviewDao.findReviewsByMovieId(movieId);
    }
}
