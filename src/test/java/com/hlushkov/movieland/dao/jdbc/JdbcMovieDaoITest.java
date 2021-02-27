package com.hlushkov.movieland.dao.jdbc;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.common.request.CreateUpdateMovieRequest;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.common.request.MovieRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcMovieDaoITest {
    @Autowired
    private JdbcMovieDao jdbcMovieDao;

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all movies from DB")
    void getAllMovies() {
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
    void getAllMoviesWithRatingDirectionTest() {
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
    void getAllMoviesWithPriceDESCDirectionTest() {
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
    void getAllMoviesWithPriceASCDirectionTest() {
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
    @DataSet(provider = TestData.MoviesProvider.class)
    @DisplayName("Returns list with all movies from DB")
    void getRandomMovies() {
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findRandomMovies();
        //then
        assertNotNull(actualMovieList);
        assertEquals(3, actualMovieList.size());
    }

    @Test
    @DataSet(provider = TestData.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre from DB")
    void getMoviesByGenre() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.of(SortDirection.ASC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findMoviesByGenre(2, movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
    }

    @Test
    @DataSet(provider = TestData.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre sorted by rating from DB")
    void getMoviesByGenreSortedByRating() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(Optional.of(SortDirection.DESC));
        movieRequest.setPriceDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findMoviesByGenre(2, movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(8.9, actualMovieList.get(0).getRating());
        assertEquals(8.8, actualMovieList.get(1).getRating());
    }

    @Test
    @DataSet(provider = TestData.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre sorted by price DESC from DB")
    void getMoviesByGenreSortedByPriceDesc() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.of(SortDirection.DESC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findMoviesByGenre(2, movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(134.67, actualMovieList.get(0).getPrice());
        assertEquals(123.45, actualMovieList.get(1).getPrice());
    }

    @Test
    @DataSet(provider = TestData.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre sorted by price ASC from DB")
    void getMoviesByGenreSortedByPriceAsc() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.of(SortDirection.ASC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findMoviesByGenre(2, movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(123.45, actualMovieList.get(0).getPrice());
        assertEquals(134.67, actualMovieList.get(1).getPrice());
    }

    @Test
    @DataSet(provider = TestData.MoviesCountriesGenresReviewsUsers.class, cleanAfter = true)
    @DisplayName("Returns Movie with details by movie id")
    void findMovieWithDetailsByMovieId() {
        //when
        MovieDetails actualMovieDetails = jdbcMovieDao.findMovieDetailsByMovieId(1);
        //then
        assertEquals(1, actualMovieDetails.getId());
        assertEquals("Побег из Шоушенка", actualMovieDetails.getNameRussian());
        assertEquals("The Shawshank Redemption", actualMovieDetails.getNameNative());
        assertEquals("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. " +
                "Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе" +
                " стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, " +
                "вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает " +
                "разрабатывать невероятно дерзкий план своего освобождения.", actualMovieDetails.getDescription());
        assertEquals(1994, actualMovieDetails.getYearOfRelease());
        assertEquals(8.9, actualMovieDetails.getRating());
        assertEquals(123.45, actualMovieDetails.getPrice());
        assertEquals("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg", actualMovieDetails.getPicturePath());

        assertEquals(1, actualMovieDetails.getGenres().get(0).getId());
        assertEquals("драма", actualMovieDetails.getGenres().get(0).getName());
        assertEquals(2, actualMovieDetails.getGenres().get(1).getId());
        assertEquals("криминал", actualMovieDetails.getGenres().get(1).getName());

        assertEquals(1, actualMovieDetails.getCountries().get(0).getId());
        assertEquals("США", actualMovieDetails.getCountries().get(0).getName());

        assertEquals(2, actualMovieDetails.getReviews().size());
        assertEquals(1, actualMovieDetails.getReviews().get(0).getId());
        assertEquals("Гениальное кино! Смотришь и думаешь «Так не бывает!», но позже понимаешь, то только так " +
                        "и должно быть. Начинаешь заново осмысливать значение фразы, которую постоянно используешь в своей жизни," +
                        " «Надежда умирает последней». Ведь если ты не надеешься, то все в твоей жизни гаснет, не остается смысла." +
                        " Фильм наполнен бесконечным числом правильных афоризмов. Я уверена, что буду пересматривать его сотни раз.",
                actualMovieDetails.getReviews().get(0).getText());
        assertEquals(2, actualMovieDetails.getReviews().get(1).getId());
        assertEquals("Кино это является, безусловно, «со знаком качества». Что же до первого места в рейтинге, " +
                        "то, думаю, здесь имело место быть выставление «десяточек» от большинства зрителей вкупе с " +
                        "раздутыми восторженными откликами кинокритиков. 'Фильм атмосферный. Он драматичный. И, конечно," +
                        " заслуживает того, чтобы находиться довольно высоко в мировом кинематографе.",
                actualMovieDetails.getReviews().get(1).getText());


        assertEquals(2, actualMovieDetails.getReviews().get(0).getUser().getId());
        assertEquals("Дарлин Эдвардс", actualMovieDetails.getReviews().get(0).getUser().getNickname());
        assertEquals(3, actualMovieDetails.getReviews().get(1).getUser().getId());
        assertEquals("Габриэль Джексон", actualMovieDetails.getReviews().get(1).getUser().getNickname());
    }

    /*FIXME*/
/*    @Test
    @DataSet(provider = TestData.MoviesCountriesGenresReviewsUsers.class,
            executeStatementsBefore = "SELECT setval('movies_movie_id_seq', 25)", cleanAfter = true)
    @ExpectedDataSet(provider = TestData.AddMovieResultProvider.class)
    @DisplayName("Adds Movie to DB")
    void addMovie() {
        //prepare
        CreateUpdateMovieRequest createUpdateMovieRequest = CreateUpdateMovieRequest.builder()
                .nameRussian("Побег из тюрьмы Шоушенка")
                .nameNative("The Shawshank Redemption prison")
                .yearOfRelease(1994)
                .description("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.")
                .rating(8.0)
                .price(123.45)
                .picturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg")
                .countriesIds(List.of(1, 2))
                .genresIds(List.of(1, 2, 3))
                .build();

        //when+then
        jdbcMovieDao.addMovie(createUpdateMovieRequest);
    }*/

/*    @Test
    @DataSet(provider = TestData.EditMovieDataProvider.class, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.EditMovieDataResultProvider.class)
    @DisplayName("Updates movie with genres and countries by movie id")
    void editMovie() {
        //prepare
        CreateUpdateMovieRequest createUpdateMovieRequest = CreateUpdateMovieRequest.builder()
                .nameRussian("Побег из тюрьмы Шоушенка")
                .nameNative("The Shawshank Redemption prison")
                .yearOfRelease(1994)
                .description("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.")
                .rating(8.0)
                .price(123.45)
                .picturePath("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg")
                .countriesIds(List.of(3, 4))
                .genresIds(List.of(4, 5, 6))
                .build();
        //when
        jdbcMovieDao.editMovie(2, createUpdateMovieRequest);
    }*/

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

/*    @Test
    @DataSet(provider = TestData.AddMoviesGenresDataProvider.class, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.AddMoviesGenresResultProvider.class)
    @DisplayName("Adds movies-genres")
    void addMoviesGenres() {
        //prepare
        MapSqlParameterSource parametersMap = new MapSqlParameterSource();
        parametersMap.addValue("movie_id", 1);
        List<Integer> countriesIds = List.of(3, 4);

        //when
        jdbcMovieDao.addMoviesGenres(parametersMap, countriesIds);
    }

    @Test
    @DataSet(provider = TestData.AddMoviesCountriesDataProvider.class, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.AddMoviesCountriesResultProvider.class)
    @DisplayName("Adds movies-countries")
    void addMoviesCountries() {
        //prepare
        MapSqlParameterSource parametersMap = new MapSqlParameterSource();
        parametersMap.addValue("movie_id", 1);
        List<Integer> countriesIds = List.of(3, 4);

        //when
        jdbcMovieDao.addMoviesCountries(parametersMap, countriesIds);
    }*/
}