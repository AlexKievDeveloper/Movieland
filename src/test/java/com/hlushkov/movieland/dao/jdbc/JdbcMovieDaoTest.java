package com.hlushkov.movieland.dao.jdbc;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.RootApplicationContext;
import com.hlushkov.movieland.TestConfiguration;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.entity.MovieRequest;
import com.hlushkov.movieland.entity.SortDirection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@SpringJUnitWebConfig(value = {RootApplicationContext.class, TestConfiguration.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcMovieDaoTest {
    @Autowired
    private JdbcMovieDao jdbcMovieDao;
    @Autowired
    private String findAllMovies;
    @Autowired
    private String findMoviesByGenre;

    @Test
    @DataSet(provider = TestConfiguration.MovieProvider.class, cleanBefore = true)
    @DisplayName("Returns list with all movies from DB")
    void getAllMovies() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        movieRequest.setPriceDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findAll(movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
/*        assertEquals("Побег из Шоушенка", actualMovieList.get(0).getNameRussian());
        assertEquals("Зеленая миля", actualMovieList.get(1).getNameRussian());*/
    }

    @Test
    @DataSet(provider = TestConfiguration.MovieProvider.class, cleanBefore = true)
    @DisplayName("Returns list with all movies from DB sorting by rating DESC")
    void getAllMoviesWithRatingDirectionTest() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(Optional.of(SortDirection.DESC));
        movieRequest.setPriceDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findAll(movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(8.9, actualMovieList.get(0).getRating());
        assertEquals(8.8, actualMovieList.get(1).getRating());
    }

    @Test
    @DataSet(provider = TestConfiguration.MovieProvider.class, cleanBefore = true)
    @DisplayName("Returns list with all movies from DB sorted by price DESC")
    void getAllMoviesWithPriceDESCDirectionTest() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.of(SortDirection.DESC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findAll(movieRequest);

        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(134.67, actualMovieList.get(0).getPrice());
        assertEquals(123.45, actualMovieList.get(1).getPrice());
    }

    @Test
    @DataSet(provider = TestConfiguration.MovieProvider.class, cleanBefore = true)
    @DisplayName("Returns list with all movies from DB sorted by price ASC")
    void getAllMoviesWithPriceASCDirectionTest() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.ofNullable(SortDirection.ASC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findAll(movieRequest);

        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(123.45, actualMovieList.get(0).getPrice());
        assertEquals(134.67, actualMovieList.get(1).getPrice());
    }

    @Test
    @DataSet(provider = TestConfiguration.MoviesProvider.class, cleanBefore = true)
    @DisplayName("Returns list with all movies from DB")
    void getRandomMovies() {
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findRandom();
        //then
        assertNotNull(actualMovieList);
        assertEquals(3, actualMovieList.size());
    }

    @Test
    @DataSet(provider = TestConfiguration.MoviesByGenresProvider.class, cleanBefore = true)
    @DisplayName("Returns list with movies by genre from DB")
    void getMoviesByGenre() {
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
    @DataSet(provider = TestConfiguration.MoviesByGenresProvider.class, cleanBefore = true)
    @DisplayName("Returns list with movies by genre sorted by rating from DB")
    void getMoviesByGenreSortedByRating() {
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
    @DataSet(provider = TestConfiguration.MoviesByGenresProvider.class, cleanBefore = true)
    @DisplayName("Returns list with movies by genre sorted by price DESC from DB")
    void getMoviesByGenreSortedByPriceDesc() {
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
    @DataSet(provider = TestConfiguration.MoviesByGenresProvider.class, cleanBefore = true)
    @DisplayName("Returns list with movies by genre sorted by price ASC from DB")
    void getMoviesByGenreSortedByPriceAsc() {
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
    @DisplayName("Returns query for find all movies")
    void returnQueryForFindAllMovies() {
        //prepare
        String expectedQuery = "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.movie_id=posters.movie_id)";

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
        String expectedQuery = "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.movie_id=posters.movie_id) ORDER BY rating DESC";

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
        String expectedQuery = "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.movie_id=posters.movie_id) ORDER BY price ASC";

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
        String expectedQuery = "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.movie_id=posters.movie_id) ORDER BY price DESC";

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
        String expectedQuery = "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath " +
                "FROM movies LEFT JOIN posters ON (movies.movie_id = posters.movie_id) " +
                "LEFT JOIN movies_genres ON (movies.movie_id = movies_genres.movie_id) " +
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
        String expectedQuery = "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath " +
                "FROM movies LEFT JOIN posters ON (movies.movie_id = posters.movie_id) " +
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
        String expectedQuery = "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath " +
                "FROM movies LEFT JOIN posters ON (movies.movie_id = posters.movie_id) " +
                "LEFT JOIN movies_genres ON (movies.movie_id = movies_genres.movie_id) " +
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
        String expectedQuery = "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath " +
                "FROM movies LEFT JOIN posters ON (movies.movie_id = posters.movie_id) " +
                "LEFT JOIN movies_genres ON (movies.movie_id = movies_genres.movie_id) " +
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
        String expectedQuery = "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath " +
                "FROM movies LEFT JOIN posters ON (movies.movie_id = posters.movie_id) " +
                "LEFT JOIN movies_genres ON (movies.movie_id = movies_genres.movie_id) " +
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