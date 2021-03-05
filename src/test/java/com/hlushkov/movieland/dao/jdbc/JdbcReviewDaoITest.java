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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcReviewDaoITest {
    @Autowired
    private JdbcReviewDao jdbcReviewDao;

    @Test
    @DisplayName("Add review to DB")
    @DataSet(provider = TestData.ReviewsProvider.class,
            executeStatementsBefore = "SELECT setval('reviews_review_id_seq', 2)", cleanAfter = true)
    @ExpectedDataSet(provider = TestData.ResultReviewsProvider.class)
    void addReview() {
        //prepare
        Review review = Review.builder()
                .movieId(1)
                .user(User.builder().id(1).build())
                .text("Nice film!")
                .build();
        //when
        jdbcReviewDao.addReview(review);
    }

    @Test
    @DisplayName("Add review to DB")
    @DataSet(provider = TestData.ReviewsProvider.class, cleanAfter = true)
    void findReviewsByMovieId() {
        //when
        List<Review> actualReviews = jdbcReviewDao.findReviewsByMovieId(1);
        //then
        assertNotNull(actualReviews);
        assertEquals(2, actualReviews.size());
        assertEquals(1, actualReviews.get(0).getId());
        assertEquals(1, actualReviews.get(0).getMovieId());
        assertEquals(2, actualReviews.get(0).getUser().getId());
        assertEquals("Гениальное кино! Смотришь и думаешь «Так не бывает!», но позже понимаешь, то " +
                "только так и должно быть. Начинаешь заново осмысливать значение фразы, которую постоянно " +
                "используешь в своей жизни, «Надежда умирает последней». Ведь если ты не надеешься, то все " +
                "в твоей жизни гаснет, не остается смысла. Фильм наполнен бесконечным числом правильных " +
                "афоризмов. Я уверена, что буду пересматривать его сотни раз.", actualReviews.get(0).getText());
        assertEquals(2, actualReviews.get(1).getId());
        assertEquals(1, actualReviews.get(1).getMovieId());
        assertEquals(3, actualReviews.get(1).getUser().getId());
        assertEquals("Кино это является, безусловно, «со знаком качества». Что же до первого места в рейтинге, " +
                "то, думаю, здесь имело место быть выставление «десяточек» от большинства зрителей " +
                "вкупе с раздутыми восторженными откликами кинокритиков. 'Фильм атмосферный. " +
                "Он драматичный. И, конечно, заслуживает того, чтобы находиться довольно высоко " +
                "в мировом кинематографе.", actualReviews.get(1).getText());
    }
}