package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.dao.jdbc.JdbcMovieDao;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.entity.MovieRequest;
import com.hlushkov.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final JdbcMovieDao jdbcMovieDao;

    @Override
    public List<Movie> getAllMovies(MovieRequest movieRequest) {
        log.info("Request for all movies in service level");
        log.info("Movies list: {}", jdbcMovieDao.getAllMovies(movieRequest));
        return jdbcMovieDao.getAllMovies(movieRequest);
    }

    @Override
    public List<Movie> getThreeRandomMovies() {
        log.info("Request for three random movies in service level");
        return jdbcMovieDao.getThreeRandomMovies();
    }

    @Override
    public List<Movie> getMoviesByGenre(int genreId) {
        return jdbcMovieDao.getMoviesByGenre(genreId);
    }
}
