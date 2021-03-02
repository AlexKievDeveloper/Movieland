package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.common.request.CreateUpdateMovieRequest;
import com.hlushkov.movieland.common.request.MovieRequest;
import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.CurrencyService;
import com.hlushkov.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;
    private final CurrencyService currencyService;

    @Override
    public List<Movie> findMovies(MovieRequest movieRequest) {
        return movieDao.findMovies(movieRequest);
    }

    @Override
    public MovieDetails findMovieDetailsByMovieId(int movieId, Optional<String> requestedCurrency) {
        MovieDetails movieDetails = movieDao.findMovieDetailsByMovieId(movieId);
        requestedCurrency.ifPresent(currency -> movieDetails.setPrice(currencyService.convert(movieDetails.getPrice(), currency)));
        return movieDetails;
    }

    @Override
    public List<Movie> findRandom() {
        return movieDao.findRandomMovies();
    }

    @Override
    public List<Movie> findByGenre(int genreId, MovieRequest movieRequest) {
        return movieDao.findMoviesByGenre(genreId, movieRequest);
    }

    @Override
    public void addMovie(CreateUpdateMovieRequest createUpdateMovieRequest) {
        movieDao.addMovie(createUpdateMovieRequest);
    }

    @Override
    public void editMovie(int movieId, CreateUpdateMovieRequest createUpdateMovieRequest) {
        movieDao.editMovie(movieId, createUpdateMovieRequest);
    }

}
