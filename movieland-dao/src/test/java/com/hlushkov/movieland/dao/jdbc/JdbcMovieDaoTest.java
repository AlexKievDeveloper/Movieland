package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.dao.config.JdbcConfig;
import com.hlushkov.movieland.entity.Movie;
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
        //when
        List<Movie> actualMovieList = jdbcMovieDao.getAllMovies();
        //then
        assertNotNull(actualMovieList);
        assertEquals(25, actualMovieList.size());
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