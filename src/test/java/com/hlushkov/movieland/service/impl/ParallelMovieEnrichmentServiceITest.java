package com.hlushkov.movieland.service.impl;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.EnrichmentType;
import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.Country;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.CountryService;
import com.hlushkov.movieland.service.GenreService;
import com.hlushkov.movieland.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@ExtendWith(MockitoExtension.class)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParallelMovieEnrichmentServiceITest {
    @Autowired
    private ParallelMovieEnrichmentService parallelMovieEnrichmentService;
    @Autowired
    private GenreService genreService;
    @Mock
    private CountryService countryService;
    @Autowired
    private ReviewService reviewService;

    @Test
    @DataSet(provider = TestData.AddMovieResultProvider.class, cleanAfter = true)
    @DisplayName("Enrich movie with details")
    void enrich() {
        //prepare
        Movie movie = Movie.builder()
                .id(1)
                .nameRussian("Побег из тюрьмы Шоушенка")
                .nameNative("The Shawshank Redemption prison")
                .yearOfRelease(1994)
                .description("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.")
                .rating(8.0)
                .price(123.45)
                .picturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg")
                .build();
        //when
        MovieDetails actualEnrichmentMovieDetails = parallelMovieEnrichmentService.enrich(movie, EnrichmentType.GENRES,
                EnrichmentType.COUNTRIES, EnrichmentType.REVIEWS);
        //then
        assertNotNull(actualEnrichmentMovieDetails.getGenres());
        assertNotNull(actualEnrichmentMovieDetails.getCountries());
        assertNotNull(actualEnrichmentMovieDetails.getReviews());

        assertEquals("США", actualEnrichmentMovieDetails.getCountries().get(0).getName());
        assertEquals("драма", actualEnrichmentMovieDetails.getGenres().get(0).getName());
        assertEquals("криминал", actualEnrichmentMovieDetails.getGenres().get(1).getName());
        assertEquals("Гениальное кино! Смотришь и думаешь «Так не бывает!», но позже понимаешь, то " +
                "только так и должно быть. Начинаешь заново осмысливать значение фразы, которую постоянно " +
                "используешь в своей жизни, «Надежда умирает последней». Ведь если ты не надеешься, то все " +
                "в твоей жизни гаснет, не остается смысла. Фильм наполнен бесконечным числом правильных " +
                "афоризмов. Я уверена, что буду пересматривать его сотни раз.", actualEnrichmentMovieDetails.getReviews().get(0).getText());
        assertEquals("Кино это является, безусловно, «со знаком качества». Что же до первого места в рейтинге, " +
                "то, думаю, здесь имело место быть выставление «десяточек» от большинства зрителей " +
                "вкупе с раздутыми восторженными откликами кинокритиков. 'Фильм атмосферный. " +
                "Он драматичный. И, конечно, заслуживает того, чтобы находиться довольно высоко " +
                "в мировом кинематографе.", actualEnrichmentMovieDetails.getReviews().get(1).getText());
    }

    @Test
    @DataSet(provider = TestData.AddMovieResultProvider.class, cleanAfter = true)
    @DisplayName("Enrich movie with details with time out during countries process")
    void enrichWithDelayForReviewEnrichment() {
        //prepare
        ParallelMovieEnrichmentService parallelMovieEnrichmentServiceWithMockReviewService =
                new ParallelMovieEnrichmentService(genreService, countryService, reviewService);
        parallelMovieEnrichmentServiceWithMockReviewService.setTimeout(5);
        Movie movie = Movie.builder()
                .id(1)
                .nameRussian("Побег из тюрьмы Шоушенка")
                .nameNative("The Shawshank Redemption prison")
                .yearOfRelease(1994)
                .description("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.")
                .rating(8.0)
                .price(123.45)
                .picturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg")
                .build();

        when(countryService.findCountriesByMovieId(1)).thenAnswer((Answer<List<Country>>) invocation -> {
            Thread.sleep(6000);
            return List.of(Country.builder().id(1).build());
        });

        //when
        MovieDetails actualEnrichmentMovieDetails = parallelMovieEnrichmentServiceWithMockReviewService.enrich(movie,
                EnrichmentType.GENRES, EnrichmentType.COUNTRIES, EnrichmentType.REVIEWS);
        //then
        assertNotNull(actualEnrichmentMovieDetails.getGenres());
        assertNull(actualEnrichmentMovieDetails.getCountries());
        assertNotNull(actualEnrichmentMovieDetails.getReviews());

        assertEquals("драма", actualEnrichmentMovieDetails.getGenres().get(0).getName());
        assertEquals("криминал", actualEnrichmentMovieDetails.getGenres().get(1).getName());
        assertEquals("Гениальное кино! Смотришь и думаешь «Так не бывает!», но позже понимаешь, то " +
                "только так и должно быть. Начинаешь заново осмысливать значение фразы, которую постоянно " +
                "используешь в своей жизни, «Надежда умирает последней». Ведь если ты не надеешься, то все " +
                "в твоей жизни гаснет, не остается смысла. Фильм наполнен бесконечным числом правильных " +
                "афоризмов. Я уверена, что буду пересматривать его сотни раз.", actualEnrichmentMovieDetails.getReviews().get(0).getText());
        assertEquals("Кино это является, безусловно, «со знаком качества». Что же до первого места в рейтинге, " +
                "то, думаю, здесь имело место быть выставление «десяточек» от большинства зрителей " +
                "вкупе с раздутыми восторженными откликами кинокритиков. 'Фильм атмосферный. " +
                "Он драматичный. И, конечно, заслуживает того, чтобы находиться довольно высоко " +
                "в мировом кинематографе.", actualEnrichmentMovieDetails.getReviews().get(1).getText());
    }
}
