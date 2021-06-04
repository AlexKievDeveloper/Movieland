package com.hlushkov.movieland.dao.cache;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.request.SaveMovieRequest;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.dao.jdbc.JdbcMovieDao;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CachedMovieDaoITest {
    @Mock
    private MovieDao mockMovieDao;
    @Autowired
    private JdbcMovieDao movieDao;

    @Test
    @DisplayName("Returns movie from cache in second time")
    void findById() {
        //prepare
        MockitoAnnotations.openMocks(this);
        CachedMovieDao cachedMovieDao = new CachedMovieDao(mockMovieDao);

        when(mockMovieDao.findById(1)).thenReturn(Movie.builder()
                .id(1)
                .nameRussian("Побег из Шоушенка")
                .nameNative("The Shawshank Redemption")
                .yearOfRelease(1994)
                .description("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, " +
                        "он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их " +
                        "рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает " +
                        "разрабатывать невероятно дерзкий план своего освобождения.")
                .price(123.45)
                .rating(8.9)
                .picturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg")
                .build());
        //when
        Movie actualMovieFirst = cachedMovieDao.findById(1);
        Movie actualMovieSecond = cachedMovieDao.findById(1);
        //then
        verify(mockMovieDao).findById(1);
        assertEquals(actualMovieFirst.hashCode(), actualMovieSecond.hashCode());
        assertEquals(actualMovieFirst, actualMovieSecond);
    }

    @Test
    @DataSet(provider = TestData.MoviesProvider.class)
    @DisplayName("Added updated movie into the cache after movie updating instead previous version")
    void editMovie() {
        MockitoAnnotations.openMocks(this);
        CachedMovieDao cachedMovieDao = new CachedMovieDao(movieDao);

        //prepare
        Movie movie = Movie.builder()
                .id(1)
                .nameRussian("Побег из Шоушенка")
                .nameNative("The Shawshank Redemption")
                .yearOfRelease(1994)
                .description("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, " +
                        "он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их " +
                        "рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает " +
                        "разрабатывать невероятно дерзкий план своего освобождения.")
                .price(123.46)
                .rating(8.9)
                .picturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg")
                .build();

        Movie movieBeforeUpdating = cachedMovieDao.findById(1);

        //when
        cachedMovieDao.editMovie(movie, movie.getId());

        //then
        Movie actualMovie = cachedMovieDao.findById(movie.getId());
        assertEquals(123.45, movieBeforeUpdating.getPrice());
        assertEquals(123.46, actualMovie.getPrice());
    }

    @Test
    @DataSet(provider = TestData.MoviesCountriesGenresReviewsUsers.class,
            executeStatementsBefore = "SELECT setval('movies_movie_id_seq', 25)", disableConstraints = true, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.SaveMovieForDaoResultProvider.class, orderBy = "movie_id")
    @DisplayName("Adds Movie to DB and to cache")
    void saveMovie() {
        //prepare
        CachedMovieDao cachedMovieDao = new CachedMovieDao(movieDao);
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
        int movieId = cachedMovieDao.saveMovie(saveMovieRequest);

        //then
        assertEquals(26, movieId);
        Movie actualMovie = cachedMovieDao.findById(movieId);
        assertEquals(123.45, actualMovie.getPrice());
    }

    @Test
    @DisplayName("Check that we get saved Movie from cache")
    void saveMovieMockMovieDao() {
        //prepare
        MockitoAnnotations.openMocks(this);
        CachedMovieDao cachedMovieDao = new CachedMovieDao(mockMovieDao);
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

        Movie movie = Movie.builder()
                .nameRussian("Побег из тюрьмы Шоушенка")
                .nameNative("The Shawshank Redemption prison")
                .yearOfRelease(1994)
                .description("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.")
                .rating(8.0)
                .price(123.45)
                .picturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg")
                .build();

        when(mockMovieDao.saveMovie(saveMovieRequest)).thenReturn(1);
        when(mockMovieDao.findById(1)).thenReturn(movie);

        //when
        cachedMovieDao.saveMovie(saveMovieRequest);
        cachedMovieDao.findById(1);

        //then
        verify(mockMovieDao).findById(1);
    }
}