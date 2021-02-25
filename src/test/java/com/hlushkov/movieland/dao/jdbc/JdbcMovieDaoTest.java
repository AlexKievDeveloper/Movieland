package com.hlushkov.movieland.dao.jdbc;

import com.github.database.rider.core.api.dataset.DataSet;
import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.common.request.AddMovieRequest;
import com.hlushkov.movieland.common.request.MovieRequest;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestWebContextConfiguration
class JdbcMovieDaoTest {
    @Autowired
    private JdbcMovieDao jdbcMovieDao;
    @Autowired
    private String findAllMovies;
    @Autowired
    private String findMoviesByGenre;

    @Test
    @DisplayName("Returns query for find all movies")
    void returnQueryForFindAllMovies() {
        //prepare
        String expectedQuery = "SELECT movies.movie_id, movie_name_russian, movie_name_native, movie_year_of_release, " +
                "movie_description, movie_rating, movie_price, movie_picture_path FROM movies";

        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.ofNullable(null));
        movieRequest.setRatingDirection(Optional.ofNullable(null));

        //when
        String actualQuery = jdbcMovieDao.generateQuery(findAllMovies, movieRequest);

        //then
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    @DisplayName("Generate query for find all movies ordered by price ASC")
    void generateQueryForAllMoviesOrderedByRatingDESC() {
        //prepare
        String expectedQuery = "SELECT movies.movie_id, movie_name_russian, movie_name_native, movie_year_of_release, " +
                "movie_description, movie_rating, movie_price, movie_picture_path FROM movies ORDER BY movie_rating desc";

        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.ofNullable(null));
        movieRequest.setRatingDirection(Optional.ofNullable(SortDirection.DESC));

        //when
        String actualQuery = jdbcMovieDao.generateQuery(findAllMovies, movieRequest);

        //then
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    @DisplayName("Generate query for find all movies ordered by price ASC")
    void generateQueryForAllMoviesOrderedByPriceASC() {
        //prepare
        String expectedQuery = "SELECT movies.movie_id, movie_name_russian, movie_name_native, movie_year_of_release, " +
                "movie_description, movie_rating, movie_price, movie_picture_path FROM movies ORDER BY movie_price asc";

        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.ofNullable(SortDirection.ASC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));

        //when
        String actualQuery = jdbcMovieDao.generateQuery(findAllMovies, movieRequest);

        //then
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    @DisplayName("Generate query for find all movies ordered by price DESC")
    void generateQueryForAllMovieOrderedByPriceDESC() {
        //prepare
        String expectedQuery = "SELECT movies.movie_id, movie_name_russian, movie_name_native, movie_year_of_release, " +
                "movie_description, movie_rating, movie_price, movie_picture_path FROM movies ORDER BY movie_price desc";

        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.ofNullable(SortDirection.DESC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));

        //when
        String actualQuery = jdbcMovieDao.generateQuery(findAllMovies, movieRequest);

        //then
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    @DisplayName("Returns query for find all movies by genre")
    void returnQueryForFindAllMoviesByGenre() {
        //prepare
        String expectedQuery = "SELECT movies.movie_id, movie_name_russian, movie_name_native, movie_year_of_release, " +
                "movie_description, movie_rating, movie_price, movie_picture_path FROM movies LEFT JOIN movies_genres " +
                "ON (movies.movie_id = movies_genres.movie_id) WHERE movies_genres.genre_id = ?";

        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.ofNullable(null));
        movieRequest.setRatingDirection(Optional.ofNullable(null));

        //when
        String actualQuery = jdbcMovieDao.generateQuery(findMoviesByGenre, movieRequest);

        //then
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    @DisplayName("Returns query for find all movies by genre with forbidden rating parameter`s value")
    void returnQueryForFindAllMoviesByGenreWithForbiddenRatingParametersValue() {
        //prepare
        String expectedQuery = "SELECT movies.movie_id, movie_name_russian, movie_name_native, movie_year_of_release, " +
                "movie_description, movie_rating, movie_price, movie_picture_path FROM movies " +
                "LEFT JOIN movies_genres ON (movies.movie_id = movies_genres.movie_id) " +
                "WHERE movies_genres.genre_id = ?";

        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.ofNullable(null));
        movieRequest.setRatingDirection(Optional.ofNullable(SortDirection.ASC));

        //when
        String actualQuery = jdbcMovieDao.generateQuery(findMoviesByGenre, movieRequest);

        //then
        assertEquals(expectedQuery, actualQuery);
    }


    @Test
    @DisplayName("Returns query for find all movies by genre sorted by rating")
    void returnQueryForFindAllMoviesByGenreSortedByRating() {
        //prepare
        String expectedQuery = "SELECT movies.movie_id, movie_name_russian, movie_name_native, movie_year_of_release, " +
                "movie_description, movie_rating, movie_price, movie_picture_path FROM movies " +
                "LEFT JOIN movies_genres ON (movies.movie_id = movies_genres.movie_id) " +
                "WHERE movies_genres.genre_id = ? ORDER BY movie_rating desc";

        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.ofNullable(null));
        movieRequest.setRatingDirection(Optional.ofNullable(SortDirection.DESC));

        //when
        String actualQuery = jdbcMovieDao.generateQuery(findMoviesByGenre, movieRequest);

        //then
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    @DisplayName("Returns query for find all movies by genre sorted by price DESC")
    void returnQueryForFindAllMoviesByGenreSortedByPriceDesc() {
        //prepare
        String expectedQuery = "SELECT movies.movie_id, movie_name_russian, movie_name_native, movie_year_of_release, " +
                "movie_description, movie_rating, movie_price, movie_picture_path FROM movies LEFT JOIN movies_genres " +
                "ON (movies.movie_id = movies_genres.movie_id) WHERE movies_genres.genre_id = ? ORDER BY movie_price desc";

        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.ofNullable(SortDirection.DESC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));

        //when
        String actualQuery = jdbcMovieDao.generateQuery(findMoviesByGenre, movieRequest);

        //then
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    @DisplayName("Returns query for find all movies by genre sorted by price ASC")
    void returnQueryForFindAllMoviesByGenreSortedByPriceAsc() {
        //prepare
        String expectedQuery = "SELECT movies.movie_id, movie_name_russian, movie_name_native, movie_year_of_release, " +
                "movie_description, movie_rating, movie_price, movie_picture_path FROM movies " +
                "LEFT JOIN movies_genres ON (movies.movie_id = movies_genres.movie_id) " +
                "WHERE movies_genres.genre_id = ? ORDER BY movie_price asc";

        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.ofNullable(SortDirection.ASC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));

        //when
        String actualQuery = jdbcMovieDao.generateQuery(findMoviesByGenre, movieRequest);

        //then
        assertEquals(expectedQuery, actualQuery);
    }


    @Test
    @DisplayName("Generate value with counties for addMovieFull query from list with country ids")
    void getRowWithCountryParameters() {
        //prepare
        String expectedString = "((SELECT movie_id_result FROM ins_movies), :country_id0), ((SELECT movie_id_result FROM ins_movies), :country_id1))";

        //when
        String actualString = jdbcMovieDao.getRowWithCountryParameters(List.of(1, 2));

        //then
        assertEquals(expectedString, actualString);
    }

    @Test
    @DisplayName("Generate value with genres for addMovieFull query from list with genre ids")
    void getRowWithGenreParameters() {
        //prepare
        String expectedString = "((SELECT movie_id_result FROM ins_movies), :genre_id0), ((SELECT movie_id_result FROM ins_movies), :genre_id1), ((SELECT movie_id_result FROM ins_movies), :genre_id2)";

        //when
        String actualString = jdbcMovieDao.getRowWithGenreParameters(List.of(1, 2, 3));

        //then
        assertEquals(expectedString, actualString);
    }

    @Test
    @DisplayName("Returns map with all entries for addMovieFullQuery")
    void getSqlParameterSource() {
        //prepare
        AddMovieRequest addMovieRequest = AddMovieRequest.builder()
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

        //when
        MapSqlParameterSource actualParameterSource = jdbcMovieDao.getSqlParameterSource(addMovieRequest);

        //then
        assertTrue(actualParameterSource.hasValue("name_russian"));
        assertTrue(actualParameterSource.hasValue("name_native"));
        assertTrue(actualParameterSource.hasValue("year_of_release"));
        assertTrue(actualParameterSource.hasValue("description"));
        assertTrue(actualParameterSource.hasValue("rating"));
        assertTrue(actualParameterSource.hasValue("price"));
        assertTrue(actualParameterSource.hasValue("picture_path"));
        assertTrue(actualParameterSource.hasValue("country_id0"));
        assertTrue(actualParameterSource.hasValue("country_id1"));
        assertTrue(actualParameterSource.hasValue("genre_id0"));
        assertTrue(actualParameterSource.hasValue("genre_id1"));
        assertTrue(actualParameterSource.hasValue("genre_id2"));

        assertEquals("Побег из тюрьмы Шоушенка", actualParameterSource.getValue("name_russian"));
        assertEquals("The Shawshank Redemption prison", actualParameterSource.getValue("name_native"));
        assertEquals(1994, actualParameterSource.getValue("year_of_release"));
        assertEquals("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.",
                actualParameterSource.getValue("description"));
        assertEquals(8.0, actualParameterSource.getValue("rating"));
        assertEquals(123.45, actualParameterSource.getValue("price"));
        assertEquals("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg",
                actualParameterSource.getValue("picture_path"));
        assertEquals(1L, actualParameterSource.getValue("country_id0"));
        assertEquals(2L, actualParameterSource.getValue("country_id1"));
        assertEquals(1L, actualParameterSource.getValue("genre_id0"));
        assertEquals(2L, actualParameterSource.getValue("genre_id1"));
        assertEquals(3L, actualParameterSource.getValue("genre_id2"));
    }

    @Test
    @DisplayName("Generate value with countries for addMovie query from list with country ids")
    void addMapSqlParametersForCountryIds() {
        //prepare
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        //when
        jdbcMovieDao.addMapSqlParametersForCountryIds(mapSqlParameterSource, List.of(1,2));

        //then
        assertTrue(mapSqlParameterSource.hasValue("country_id0"));
        assertTrue(mapSqlParameterSource.hasValue("country_id1"));

        assertEquals(1L, mapSqlParameterSource.getValue("country_id0"));
        assertEquals(2L, mapSqlParameterSource.getValue("country_id1"));
    }

    @Test
    @DataSet(provider = TestData.MovieProvider.class, cleanAfter = true)
    @DisplayName("Generate value with genres for addMovie query from list with genre ids")
    void addMapSqlParametersForGenreIds() {
        //prepare
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        //when
        jdbcMovieDao.addMapSqlParametersForGenreIds(mapSqlParameterSource, List.of(1,2,3));

        //then
        assertTrue(mapSqlParameterSource.hasValue("genre_id0"));
        assertTrue(mapSqlParameterSource.hasValue("genre_id1"));
        assertTrue(mapSqlParameterSource.hasValue("genre_id2"));

        assertEquals(1L, mapSqlParameterSource.getValue("genre_id0"));
        assertEquals(2L, mapSqlParameterSource.getValue("genre_id1"));
        assertEquals(3L, mapSqlParameterSource.getValue("genre_id2"));
    }
}
