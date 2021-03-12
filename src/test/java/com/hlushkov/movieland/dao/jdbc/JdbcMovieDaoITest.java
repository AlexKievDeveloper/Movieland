package com.hlushkov.movieland.dao.jdbc;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.common.request.MovieRequest;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcMovieDaoITest {
    @Autowired
    private JdbcMovieDao jdbcMovieDao;

    @Test
    @DataSet(provider = TestData.MoviesCountriesGenresReviewsUsers.class,
            executeStatementsBefore = "SELECT setval('movies_movie_id_seq', 25)", cleanAfter = true)
    @ExpectedDataSet(provider = TestData.SaveMovieForDaoResultProvider.class)
    @DisplayName("Adds Movie to DB")
    void saveMovie() {
        //prepare
        Movie movie = Movie.builder()
                .nameRussian("Побег из тюрьмы Шоушенка")
                .nameNative("The Shawshank Redemption prison")
                .yearOfRelease(1994)
                .description("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.")
                .rating(8.0)
                .price(123.45)
                .picturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg")
                .build();

        //when+then
        jdbcMovieDao.saveMovie(movie);
    }

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all movies from DB")
    void findMovies() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        movieRequest.setPriceDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findMovies(movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
    }

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all movies from DB sorting by rating DESC")
    void findMoviesWithRatingDirectionTest() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(Optional.of(SortDirection.DESC));
        movieRequest.setPriceDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findMovies(movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(8.9, actualMovieList.get(0).getRating());
        assertEquals(8.8, actualMovieList.get(1).getRating());
    }

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all movies from DB sorted by price DESC")
    void findMoviesWithPriceDESCDirectionTest() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.of(SortDirection.DESC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findMovies(movieRequest);

        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(134.67, actualMovieList.get(0).getPrice());
        assertEquals(123.45, actualMovieList.get(1).getPrice());
    }

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all movies from DB sorted by price ASC")
    void findMoviesWithPriceASCDirectionTest() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.ofNullable(SortDirection.ASC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findMovies(movieRequest);

        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(123.45, actualMovieList.get(0).getPrice());
        assertEquals(134.67, actualMovieList.get(1).getPrice());
    }

    @Test
    @DataSet(provider = TestData.MoviesProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all movies from DB")
    void findRandomMovies() {
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findRandom();
        //then
        assertNotNull(actualMovieList);
        assertEquals(3, actualMovieList.size());
    }

    @Test
    @DataSet(provider = TestData.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre from DB")
    void findMoviesByGenre() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.of(SortDirection.ASC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findByGenre(2, movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
    }

    @Test
    @DataSet(provider = TestData.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre sorted by rating from DB")
    void findMoviesByGenreSortedByRating() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(Optional.of(SortDirection.DESC));
        movieRequest.setPriceDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findByGenre(2, movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(8.9, actualMovieList.get(0).getRating());
        assertEquals(8.8, actualMovieList.get(1).getRating());
    }

    @Test
    @DataSet(provider = TestData.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre sorted by price DESC from DB")
    void findMoviesByGenreSortedByPriceDesc() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.of(SortDirection.DESC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findByGenre(2, movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(134.67, actualMovieList.get(0).getPrice());
        assertEquals(123.45, actualMovieList.get(1).getPrice());
    }

    @Test
    @DataSet(provider = TestData.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre sorted by price ASC from DB")
    void findMoviesByGenreSortedByPriceAsc() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.of(SortDirection.ASC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findByGenre(2, movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(123.45, actualMovieList.get(0).getPrice());
        assertEquals(134.67, actualMovieList.get(1).getPrice());
    }

    @Test
    @DataSet(provider = TestData.MoviesCountriesGenresReviewsUsers.class, cleanAfter = true)
    @DisplayName("Returns Movie with details by movie id")
    void findById() {
        //when
        Movie actualMovie = jdbcMovieDao.findById(1);
        //then
        assertEquals(1, actualMovie.getId());
        assertEquals("Побег из Шоушенка", actualMovie.getNameRussian());
        assertEquals("The Shawshank Redemption", actualMovie.getNameNative());
        assertEquals("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. " +
                "Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе" +
                " стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, " +
                "вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает " +
                "разрабатывать невероятно дерзкий план своего освобождения.", actualMovie.getDescription());
        assertEquals(1994, actualMovie.getYearOfRelease());
        assertEquals(8.9, actualMovie.getRating());
        assertEquals(123.45, actualMovie.getPrice());
        assertEquals("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg", actualMovie.getPicturePath());
    }

//FIXME

/*    @Test
    @DataSet(provider = TestData.AddMoviesGenresDataProvider.class, cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.AddMoviesGenresResultProvider.class)
    @DisplayName("Adds movies-genres")
    void addMoviesGenres() {
        //prepare
        List<Integer> genresIds = List.of(3, 4);

        //when
        jdbcMovieDao.addMoviesGenres(1, genresIds);
    }
//FIXME

    @Test
    @DataSet(provider = TestData.AddMoviesCountriesDataProvider.class, cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.AddMoviesCountriesResultProvider.class)
    @DisplayName("Adds movies-countries")
    void addMoviesCountries() {
        //prepare
        List<Integer> countryIds = List.of(3, 4);

        //when
        jdbcMovieDao.addMoviesCountries(1, countryIds);
    }*/

    @Test
    @DataSet(provider = TestData.EditMovieDataProvider.class, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.EditMovieDataResultProvider.class)
    @DisplayName("Edit movie")
    void editMovie() {
        //prepare
        Movie movie = Movie.builder()
                .id(2)
                .nameRussian("Побег из тюрьмы Шоушенка")
                .nameNative("The Shawshank Redemption prison")
                .yearOfRelease(1994)
                .description("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, " +
                        "он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их " +
                        "рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает " +
                        "разрабатывать невероятно дерзкий план своего освобождения.")
                .rating(8.0)
                .price(123.45)
                .picturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg")
                .build();
        //when+then
        jdbcMovieDao.editMovie(movie);
    }

    @Test
    @DataSet(provider = TestData.AddMoviesGenresDataProvider.class, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.EditMoviesGenresResultProvider.class)
    @DisplayName("Edit movie genres")
    void editMovieGenres() {
        //prepare
        List<Integer> genresIds = List.of(3, 4);

        //when+then
        jdbcMovieDao.editMovieGenres(1, genresIds);
    }

    @Test
    @DataSet(provider = TestData.AddMoviesCountriesDataProvider.class, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.EditMoviesCountriesResultProvider.class)
    @DisplayName("Edit movie countries")
    void editMovieCountries() {
        //prepare
        List<Integer> genresIds = List.of(3, 4);

        //when+then
        jdbcMovieDao.editMovieCountries(1, genresIds);
    }

    @Test
    @DataSet(provider = TestData.RemoveMoviesGenresDataProvider.class, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.RemoveMoviesGenresResultProvider.class)
    @DisplayName("Removes movies-genres by movie id")
    void removeMoviesGenresByMovieId() {
        //when
        jdbcMovieDao.removeMoviesGenresByMovieId(1);
    }

    @Test
    @DataSet(provider = TestData.RemoveMoviesCountriesDataProvider.class, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.RemoveMoviesCountriesResultProvider.class)
    @DisplayName("Removes movies-countries by movie id")
    void removeMoviesCountriesByMovieId() {
        //when
        jdbcMovieDao.removeMoviesCountriesByMovieId(1);
    }

    @Test
    @DataSet(provider = TestData.ReviewsProvider.class, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.RemoveReviewsResultProvider.class)
    @DisplayName("Remove reviews by movie id and returns true")
    void removeReviewsByMovieId() {
        //when
        assertTrue(jdbcMovieDao.removeReviewsByMovieId(1));
    }

    @Test
    @DataSet(provider = TestData.ReviewsProvider.class, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.RemoveReviewsResultProvider.class)
    @DisplayName("Returns false if no (requested for removing) data exist")
    void removeReviewsByMovieIdIfMovieDoesNotExists() {
        //when
        assertFalse(jdbcMovieDao.removeReviewsByMovieId(100));
    }

}