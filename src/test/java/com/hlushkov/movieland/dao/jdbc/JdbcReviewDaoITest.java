package com.hlushkov.movieland.dao.jdbc;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.Review;
import com.hlushkov.movieland.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcReviewDaoITest {
    @Autowired
    private JdbcReviewDao jdbcReviewDao;

    @Test
    @DisplayName("Save review to DB")
    @DataSet(provider = TestData.ReviewsProvider.class, executeStatementsBefore = "SELECT setval('reviews_review_id_seq', 2)")
    @ExpectedDataSet(provider = TestData.ResultReviewsProvider.class)
    void save() {
        //prepare
        Review review = Review.builder()
                .movieId(1)
                .user(User.builder().id(1).build())
                .text("Nice film!")
                .build();
        //when
        jdbcReviewDao.save(review);
    }
}