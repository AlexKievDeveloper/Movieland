package com.hlushkov.movieland.service.impl;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.Currency;
import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.service.CurrencyService;
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
    private CurrencyService currencyService;

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("")
    void findMovieDetailsByMovieIdWithSpecifiedUsdCurrency() {
        //prepare
        double price = 123.45 / currencyService.getCurrencyExchangeRate(Currency.USD);
        double expectedPrice = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
        //when
        MovieDetails actualMovieDetails = movieService.findMovieDetailsByMovieId(1, Optional.of(Currency.USD));
        //then
        assertEquals(expectedPrice, actualMovieDetails.getPrice());
    }

}