package com.hlushkov.movieland.dao.jdbc;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.config.TestConfiguration;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
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
    @DataSet(provider = TestConfiguration.ReviewsProvider.class, executeStatementsBefore = "SELECT setval('reviews_review_id_seq', 2)")
    @ExpectedDataSet(provider = TestConfiguration.ResultReviewsProvider.class)
    void addReview() {
        //when
        jdbcReviewDao.addReview(1, 1, "Nice film!");
    }
}