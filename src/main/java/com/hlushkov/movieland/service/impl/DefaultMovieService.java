package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.common.request.AddMovieRequest;
import com.hlushkov.movieland.common.request.MovieRequest;
import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;

    @Override
    public List<Movie> findMovies(MovieRequest movieRequest) {
        return movieDao.findMovies(movieRequest);
    }

    @Override
    public MovieDetails findMovieDetailsByMovieId(int movieId) {
        return movieDao.findMovieDetailsByMovieId(movieId);
    }

    @Override
    public List<Movie> findRandom() {
        return movieDao.findRandomMovies();
    }

    @Override
    public List<Movie> findByGenre(int genreId, MovieRequest movieRequest) {
        return movieDao.findMoviesByGenre(genreId, movieRequest);
    }

  /*FIXME  @Transactional*/
    @Override
    public void addMovie(AddMovieRequest addMovieRequest) {
        movieDao.addMovie(addMovieRequest);
    }

}
