package com.hlushkov.movieland.service.impl;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.Review;
import com.hlushkov.movieland.service.CountryService;
import com.hlushkov.movieland.service.CurrencyService;
import com.hlushkov.movieland.service.GenreService;
import com.hlushkov.movieland.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@ExtendWith(MockitoExtension.class)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultMovieServiceMultithreadingITest {

    @Autowired
    private MovieDao movieDao;
    @Autowired
    private GenreService genreService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private CurrencyService currencyService;
    @Mock
    private ReviewService reviewService;

    @Test
    @DataSet(provider = TestData.AddMovieResultProvider.class, cleanAfter = true)
    @DisplayName("Returns movie details without reviews")
    void findById() {
        //prepare
        MockitoAnnotations.openMocks(this);

        DefaultMovieService movieService = new DefaultMovieService(movieDao, countryService, genreService, reviewService, currencyService);
        movieService.setThreadInterruptingPeriod(5000);

        when(reviewService.findReviewsByMovieId(1)).thenAnswer((Answer<List<Review>>) invocation -> List.of(Review.builder().id(1).build()));
        //when
        MovieDetails actualMovieDetails = movieService.findById(1, Optional.ofNullable(null));

        //then
        assertNotNull(actualMovieDetails.getGenres());
        assertNotNull(actualMovieDetails.getCountries());
        assertNotNull(actualMovieDetails.getReviews());

        assertEquals("США", actualMovieDetails.getCountries().get(0).getName());
        assertEquals("драма", actualMovieDetails.getGenres().get(0).getName());
        assertEquals("криминал", actualMovieDetails.getGenres().get(1).getName());
        assertEquals(1, actualMovieDetails.getReviews().get(0).getId());
    }

    @Test
    @DataSet(provider = TestData.AddMovieResultProvider.class, cleanAfter = true)
    @DisplayName("Returns movie details without reviews")
    void findByIdThrowsTimeoutExceptionWhileLoadingReviews() {
        //prepare
        MockitoAnnotations.openMocks(this);

        DefaultMovieService movieService = new DefaultMovieService(movieDao, countryService, genreService, reviewService, currencyService);
        movieService.setThreadInterruptingPeriod(5000);

        when(reviewService.findReviewsByMovieId(1)).thenAnswer((Answer<List<Review>>) invocation -> {
            Thread.sleep(6000);
            return List.of(Review.builder().id(1).build());
        });
        //when
        MovieDetails actualMovieDetails = movieService.findById(1, Optional.ofNullable(null));

        //then
        assertNotNull(actualMovieDetails.getGenres());
        assertNotNull(actualMovieDetails.getCountries());
        assertNull(actualMovieDetails.getReviews());

        assertEquals("США", actualMovieDetails.getCountries().get(0).getName());
        assertEquals("драма", actualMovieDetails.getGenres().get(0).getName());
        assertEquals("криминал", actualMovieDetails.getGenres().get(1).getName());
    }

}
