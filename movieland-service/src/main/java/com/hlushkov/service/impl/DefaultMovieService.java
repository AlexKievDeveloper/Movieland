package com.hlushkov.service.impl;

import com.hlushkov.dao.jdbc.JdbcMovieDao;
import com.hlushkov.service.GenreService;
import com.hlushkov.service.MovieService;
import entity.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final JdbcMovieDao jdbcMovieDao;
    private final GenreService genreService;

    @Override
    public List<Movie> getAllMovies() {
        log.info("Request for all movies in service level");
        return jdbcMovieDao.getAllMovies();
    }

    @Override
    public List<Movie> getThreeRandomMovies() {
        log.info("Request for three random movies in service level");
        return jdbcMovieDao.getThreeRandomMovies();
    }

    @Override
    public List<Movie> getMoviesByGenre(int genreId) {
        /*String genreName = "'" + genreService.getGenreNameById(genreId) + "'";*/
        return jdbcMovieDao.getMoviesByGenre(genreId);
    }
}