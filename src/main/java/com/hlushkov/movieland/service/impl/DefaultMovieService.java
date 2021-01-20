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
    public List<Movie> findAllMovies(MovieRequest movieRequest) {
        log.info("Request for all movies in service level");
        return jdbcMovieDao.findAllMovies(movieRequest);
    }

    @Override
    public List<Movie> findRandomMovies() {
        log.info("Request for three random movies in service level");
        return jdbcMovieDao.findRandomMovies();
    }

    @Override
    public List<Movie> findMoviesByGenre(int genreId, MovieRequest movieRequest) {
        log.info("Request for movies by genre in service level");
        return jdbcMovieDao.findMoviesByGenre(genreId, movieRequest);
    }
}
