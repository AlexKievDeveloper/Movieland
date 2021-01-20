package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.RootApplicationContext;
import com.hlushkov.movieland.TestConfiguration;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.entity.MovieRequest;
import com.hlushkov.movieland.entity.SortDirection;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitWebConfig(value = {TestConfiguration.class, RootApplicationContext.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcMovieDaoTest {
    @Autowired
    private JdbcMovieDao jdbcMovieDao;

    @Autowired
    private Flyway flyway;

    @BeforeAll
    void setUpDb() {
        flyway.migrate();
    }

    @Test
    @DisplayName("Returns list with all movies from DB")
    void getAllMovies() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(SortDirection.DESC);
        //when
        List<Movie> actualMovieList = jdbcMovieDao.getAllMovies(movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(25, actualMovieList.size());
    }

    @Test
    @DisplayName("Returns list with all movies from DB sorting by rating DESC")
    void getAllMoviesWithRatingDirectionTest() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(SortDirection.DESC);
        //when
        List<Movie> actualMovieList = jdbcMovieDao.getAllMovies(movieRequest);

        //then
        assertNotNull(actualMovieList);
        assertEquals(25, actualMovieList.size());
        assertEquals(8.9, actualMovieList.get(0).getRating());
        assertEquals(7.6, actualMovieList.get(actualMovieList.size() - 1).getRating());
    }

    @Test
    @DisplayName("Returns list with all movies from DB sorted by price DESC")
    void getAllMoviesWithPriceDESCDirectionTest() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(SortDirection.DESC);
        //when
        List<Movie> actualMovieList = jdbcMovieDao.getAllMovies(movieRequest);

        //then
        assertNotNull(actualMovieList);
        assertEquals(25, actualMovieList.size());
        assertEquals(200.6, actualMovieList.get(0).getPrice());
        assertEquals(100.0, actualMovieList.get(actualMovieList.size() - 1).getPrice());
    }

    @Test
    @DisplayName("Returns list with all movies from DB sorted by price ASC")
    void getAllMoviesWithPriceASCDirectionTest() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(SortDirection.ASC);
        //when
        List<Movie> actualMovieList = jdbcMovieDao.getAllMovies(movieRequest);

        //then
        assertNotNull(actualMovieList);
        assertEquals(25, actualMovieList.size());
        assertEquals(100.0, actualMovieList.get(0).getPrice());
        assertEquals(200.6, actualMovieList.get(actualMovieList.size() - 1).getPrice());
    }


    @Test
    @DisplayName("Returns list with all movies from DB")
    void getThreeRandomMovies() {
        //when
        List<Movie> actualMovieList = jdbcMovieDao.getRandomMovies();
        //then
        assertNotNull(actualMovieList);
        assertEquals(3, actualMovieList.size());
    }

    @Test
    @DisplayName("Returns list with movies by genre from DB")
    void getMoviesByGenre() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(SortDirection.ASC);
        //when
        List<Movie> actualMovieList = jdbcMovieDao.getMoviesByGenre(15, movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(3, actualMovieList.size());
    }

    @Test
    @DisplayName("Returns list with movies by genre sorted by rating from DB")
    void getMoviesByGenreSortedByRating() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(SortDirection.DESC);
        //when
        List<Movie> actualMovieList = jdbcMovieDao.getMoviesByGenre(15, movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(3, actualMovieList.size());
        assertEquals(8.5, actualMovieList.get(0).getRating());
        assertEquals(8.5, actualMovieList.get(1).getRating());
        assertEquals(8.0, actualMovieList.get(2).getRating());
    }

    @Test
    @DisplayName("Returns list with movies by genre sorted by price DESC from DB")
    void getMoviesByGenreSortedByPriceDesc() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(SortDirection.DESC);
        //when
        List<Movie> actualMovieList = jdbcMovieDao.getMoviesByGenre(15, movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(3, actualMovieList.size());
        assertEquals(170, actualMovieList.get(0).getPrice());
        assertEquals(130, actualMovieList.get(1).getPrice());
        assertEquals(120.55, actualMovieList.get(2).getPrice());
    }

    @Test
    @DisplayName("Returns list with movies by genre sorted by price DESC from DB")
    void getMoviesByGenreSortedByPriceAsc() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(SortDirection.ASC);
        //when
        List<Movie> actualMovieList = jdbcMovieDao.getMoviesByGenre(15, movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(3, actualMovieList.size());
        assertEquals(120.55, actualMovieList.get(0).getPrice());
        assertEquals(130, actualMovieList.get(1).getPrice());
        assertEquals(170, actualMovieList.get(2).getPrice());
    }

}