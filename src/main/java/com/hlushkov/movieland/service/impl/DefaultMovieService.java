package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.dao.MovieDao;
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
    private final MovieDao jdbcMovieDao;

    @Override
    public List<Movie> findAll(MovieRequest movieRequest) {
        log.info("Request for all movies in service level");
        return jdbcMovieDao.findAll(movieRequest);
    }

    @Override
    public List<Movie> findRandom() {
        log.info("Request for three random movies in service level");
        return jdbcMovieDao.findRandom();
    }

    @Override
    public List<Movie> findByGenre(int genreId, MovieRequest movieRequest) {
        log.info("Request for movies by genre in service level");
        return jdbcMovieDao.findByGenre(genreId, movieRequest);
    }
}
