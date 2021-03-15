package com.hlushkov.movieland.service.impl;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.common.request.SaveMovieRequest;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.service.MovieService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@ExtendWith(MockitoExtension.class)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultMovieServiceITest {
    @Autowired
    private MovieService movieService;
    @Autowired
    private DefaultCurrencyService currencyService;

    @Test
    @DataSet(provider = TestData.SaveMovieServiceProvider.class,
            executeStatementsBefore = "SELECT setval('movies_movie_id_seq', 1)", disableConstraints = true, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.SaveMovieServiceResultProvider.class, orderBy = "movie_id")
    @DisplayName("Returns movie details with converted price")
    void saveMovie() {
        //prepare
        SaveMovieRequest saveMovieRequest = SaveMovieRequest.builder()
                .nameRussian("Побег из тюрьмы Шоушенка")
                .nameNative("The Shawshank Redemption prison")
                .yearOfRelease(1994)
                .description("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.")
                .rating(8.0)
                .price(123.45)
                .picturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg")
                .genresIds(List.of(3, 4))
                .countriesIds(List.of(3, 4))
                .build();
        //when
        movieService.saveMovie(saveMovieRequest);
    }

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns movie details with converted price")
    void findByIdWithSpecifiedUsdCurrency() {
        //prepare
        double expectedPrice = 123.45 / currencyService.getCurrencyExchangeRate("USD");
        //when
        MovieDetails actualMovieDetails = movieService.findById(1, Optional.of("USD"));
        //then
        assertEquals(expectedPrice, actualMovieDetails.getPrice());
    }

    @Test
    @DataSet(provider = TestData.AddMovieResultProvider.class, cleanAfter = true)
    @DisplayName("Returns movie details")
    void findById() {
        //when
        MovieDetails actualMovieDetails = movieService.findById(1, Optional.ofNullable(null));

        //then
        assertNotNull(actualMovieDetails.getGenres());
        assertNotNull(actualMovieDetails.getReviews());
        assertNotNull(actualMovieDetails.getCountries());

        assertEquals("США", actualMovieDetails.getCountries().get(0).getName());
        assertEquals("драма", actualMovieDetails.getGenres().get(0).getName());
        assertEquals("криминал", actualMovieDetails.getGenres().get(1).getName());
        assertEquals(1, actualMovieDetails.getReviews().get(0).getId());
        assertEquals(1, actualMovieDetails.getReviews().get(0).getMovieId());
        assertEquals(2, actualMovieDetails.getReviews().get(0).getUserId());
        assertEquals("Гениальное кино! Смотришь и думаешь «Так не бывает!», но позже понимаешь, то только так и должно быть. Начинаешь заново осмысливать значение фразы, которую постоянно используешь в своей жизни, «Надежда умирает последней». Ведь если ты не надеешься, то все в твоей жизни гаснет, не остается смысла. Фильм наполнен бесконечным числом правильных афоризмов. Я уверена, что буду пересматривать его сотни раз.", actualMovieDetails.getReviews().get(0).getText());
        assertEquals(2, actualMovieDetails.getReviews().get(1).getId());
        assertEquals(1, actualMovieDetails.getReviews().get(1).getMovieId());
        assertEquals(3, actualMovieDetails.getReviews().get(1).getUserId());
        assertEquals("Кино это является, безусловно, «со знаком качества». Что же до первого места в рейтинге, то, думаю, здесь имело место быть выставление «десяточек» от большинства зрителей вкупе с раздутыми восторженными откликами кинокритиков. 'Фильм атмосферный. Он драматичный. И, конечно, заслуживает того, чтобы находиться довольно высоко в мировом кинематографе.", actualMovieDetails.getReviews().get(1).getText());
    }

}