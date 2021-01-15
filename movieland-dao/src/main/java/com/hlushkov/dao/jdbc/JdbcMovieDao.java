package com.hlushkov.dao.jdbc;

import com.hlushkov.dao.MovieDao;
import com.hlushkov.dao.jdbc.mapper.MovieRowMapper;
import entity.Movie;
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
        //Random random = new Random();

        for (int i = 0; i < 3; i++) {
            //int randomNumber = random.nextInt(allMoviesList.size());
            int randomNumber = ThreadLocalRandom.current().nextInt(allMoviesList.size());
            randomMovieList.add(allMoviesList.get(randomNumber));
            allMoviesList.remove(randomNumber);
        }

        return randomMovieList;
    }
}
