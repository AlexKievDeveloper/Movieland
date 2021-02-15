package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.request.MovieRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        String expectedQuery = "SELECT movies.id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.id=posters.movie_id)";

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
        String expectedQuery = "SELECT movies.id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.id=posters.movie_id) ORDER BY rating DESC";

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
        String expectedQuery = "SELECT movies.id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.id=posters.movie_id) ORDER BY price ASC";

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
        String expectedQuery = "SELECT movies.id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.id=posters.movie_id) ORDER BY price DESC";

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
        String expectedQuery = "SELECT movies.id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath " +
                "FROM movies LEFT JOIN posters ON (movies.id = posters.movie_id) " +
                "LEFT JOIN movies_genres ON (movies.id = movies_genres.movie_id) " +
                "WHERE movies_genres.genre_id = ?";

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
        String expectedQuery = "SELECT movies.id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath " +
                "FROM movies LEFT JOIN posters ON (movies.id = posters.movie_id) " +
                "LEFT JOIN movies_genres ON (movies.id = movies_genres.movie_id) " +
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
        String expectedQuery = "SELECT movies.id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath " +
                "FROM movies LEFT JOIN posters ON (movies.id = posters.movie_id) " +
                "LEFT JOIN movies_genres ON (movies.id = movies_genres.movie_id) " +
                "WHERE movies_genres.genre_id = ? ORDER BY rating DESC";

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
        String expectedQuery = "SELECT movies.id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath " +
                "FROM movies LEFT JOIN posters ON (movies.id = posters.movie_id) " +
                "LEFT JOIN movies_genres ON (movies.id = movies_genres.movie_id) " +
                "WHERE movies_genres.genre_id = ? ORDER BY price DESC";

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
        String expectedQuery = "SELECT movies.id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath " +
                "FROM movies LEFT JOIN posters ON (movies.id = posters.movie_id) " +
                "LEFT JOIN movies_genres ON (movies.id = movies_genres.movie_id) " +
                "WHERE movies_genres.genre_id = ? ORDER BY price ASC";

        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.ofNullable(SortDirection.ASC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));

        //when
        String actualQuery = jdbcMovieDao.generateQuery(findMoviesByGenre, movieRequest);

        //then
        assertEquals(expectedQuery, actualQuery);
    }
}
