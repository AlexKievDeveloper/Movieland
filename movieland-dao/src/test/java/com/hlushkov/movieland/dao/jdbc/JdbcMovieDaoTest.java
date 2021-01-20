package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.dao.config.JdbcConfig;
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

import static org.junit.jupiter.api.Assertions.*;
@SpringJUnitWebConfig(value = {TestConfiguration.class, JdbcConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcMovieDaoTest {
    @Autowired
    private JdbcMovieDao jdbcMovieDao;

    @Autowired
    private Flyway flyway;

    @BeforeAll
    void setUpDb(){
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
        assertEquals(7.6, actualMovieList.get(actualMovieList.size()-1).getRating());
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
        assertEquals(100.0, actualMovieList.get(actualMovieList.size()-1).getPrice());
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
        assertEquals(200.6, actualMovieList.get(actualMovieList.size()-1).getPrice());
    }


    @Test
    @DisplayName("Returns list with all movies from DB")
    void getThreeRandomMovies() {
        //when
        List<Movie> actualMovieList = jdbcMovieDao.getThreeRandomMovies();
        //then
        assertNotNull(actualMovieList);
        assertEquals(3, actualMovieList.size());
    }

    @Test
    @DisplayName("Returns list with all movies from DB")
    void getMoviesByGenre() {
        //when
        List<Movie> actualMovieList = jdbcMovieDao.getMoviesByGenre(15);
        //then
        assertNotNull(actualMovieList);
        assertEquals(3, actualMovieList.size());
    }

}