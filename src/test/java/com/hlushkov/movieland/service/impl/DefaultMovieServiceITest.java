package com.hlushkov.movieland.service.impl;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.service.MovieService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns movie details with converted price")
    void findMovieDetailsByMovieIdWithSpecifiedUsdCurrency() {
        //prepare
        double price = 123.45 / currencyService.getCurrencyExchangeRate("USD");
        double expectedPrice = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
        //when
        MovieDetails actualMovieDetails = movieService.findMovieDetailsByMovieId(1, Optional.of("USD"));
        //then
        assertEquals(expectedPrice, actualMovieDetails.getPrice());
    }

    //FIXME
    @Test
    @DataSet(provider = TestData.AddMovieResultProvider.class, cleanAfter = true)
    @DisplayName("Returns movie details")
    void findMovieDetailsByMovieId() {
        //when
        MovieDetails actualMovieDetails = movieService.findMovieDetailsByMovieId(1, Optional.ofNullable(null));
        System.out.println("Actual movie details: " +  actualMovieDetails);
        //then
        assertNotNull(actualMovieDetails.getCountries());
        assertNotNull(actualMovieDetails.getGenres());
        assertNotNull(actualMovieDetails.getReviews());

        assertEquals("США", actualMovieDetails.getCountries().get(0).getName());
        assertEquals("драма", actualMovieDetails.getGenres().get(0).getName());
        assertEquals("криминал", actualMovieDetails.getGenres().get(1).getName());
        assertEquals(1, actualMovieDetails.getReviews().get(0).getId());
        assertEquals(1, actualMovieDetails.getReviews().get(0).getMovieId());
        assertEquals(2, actualMovieDetails.getReviews().get(0).getUser().getId());
        assertEquals("Гениальное кино! Смотришь и думаешь «Так не бывает!», но позже понимаешь, то только так и должно быть. Начинаешь заново осмысливать значение фразы, которую постоянно используешь в своей жизни, «Надежда умирает последней». Ведь если ты не надеешься, то все в твоей жизни гаснет, не остается смысла. Фильм наполнен бесконечным числом правильных афоризмов. Я уверена, что буду пересматривать его сотни раз.", actualMovieDetails.getReviews().get(0).getText());
        assertEquals(2, actualMovieDetails.getReviews().get(1).getId());
        assertEquals(1, actualMovieDetails.getReviews().get(1).getMovieId());
        assertEquals(3, actualMovieDetails.getReviews().get(1).getUser().getId());
        assertEquals("Кино это является, безусловно, «со знаком качества». Что же до первого места в рейтинге, то, думаю, здесь имело место быть выставление «десяточек» от большинства зрителей вкупе с раздутыми восторженными откликами кинокритиков. 'Фильм атмосферный. Он драматичный. И, конечно, заслуживает того, чтобы находиться довольно высоко в мировом кинематографе.", actualMovieDetails.getReviews().get(1).getText());
    }

}