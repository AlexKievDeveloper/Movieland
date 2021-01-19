package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.hlushkov.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcMovieDao implements MovieDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private String getAllMovies;

    @Autowired
    private String getGetMoviesByGenre;

    @Override
    public List<Movie> getAllMovies() {
        log.info("Request for all movies in dao level");
        return jdbcTemplate.query(getAllMovies, new MovieRowMapper());
    }

    @Override
    public List<Movie> getThreeRandomMovies() {
        log.info("Request for three random movies in dao level");
        List<Movie> allMoviesList = getAllMovies();
        List<Movie> randomMovieList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            int randomNumber = ThreadLocalRandom.current().nextInt(allMoviesList.size());
            randomMovieList.add(allMoviesList.get(randomNumber));
            allMoviesList.remove(randomNumber);
        }

        return randomMovieList;
    }

    @Override
    public List<Movie> getMoviesByGenre(int genreId) {
        return jdbcTemplate.query(getGetMoviesByGenre, new MovieRowMapper(), genreId);
    }
}
