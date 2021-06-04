package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.common.EnrichmentType;
import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.common.request.FindMoviesRequest;
import com.hlushkov.movieland.common.request.SaveMovieRequest;
import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.CurrencyService;
import com.hlushkov.movieland.service.MovieEnrichmentService;
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
    private final MovieEnrichmentService movieEnrichmentService;
    private final CurrencyService currencyService;

    @Override
    public void saveMovie(SaveMovieRequest saveMovieRequest) {
        movieDao.saveMovie(saveMovieRequest);
    }

    @Override
    public List<Movie> findMovies(FindMoviesRequest findMoviesRequest) {
        return movieDao.findMovies(findMoviesRequest);
    }

    @Override
    public List<Movie> findRandom() {
        return movieDao.findRandom();
    }

    @Override
    public List<Movie> findByGenre(int genreId, FindMoviesRequest findMoviesRequest) {
        return movieDao.findByGenre(genreId, findMoviesRequest);
    }

    @Override
    public MovieDetails findById(int movieId, Optional<String> requestedCurrency) {
        Movie movie = movieDao.findById(movieId);
        MovieDetails movieDetails = movieEnrichmentService.enrich(movie, EnrichmentType.GENRES,
                EnrichmentType.COUNTRIES, EnrichmentType.REVIEWS);
        requestedCurrency.ifPresent(currency -> movieDetails.setPrice(currencyService.convert(movie.getPrice(), currency)));
        log.debug("MovieDetails: {}", movieDetails);
        return movieDetails;
    }

    @Override
    public void editMovie(Movie movie, Integer movieId) {
        movieDao.editMovie(movie, movieId);
    }

    @Override
    public void editMovieGenres(Integer movieId, List<Integer> genreIds) {
        movieDao.editMovieGenres(movieId, genreIds);
    }

    @Override
    public void editMovieCountries(Integer movieId, List<Integer> countryIds) {
        movieDao.editMovieCountries(movieId, countryIds);
    }

    @Override
    public boolean removeReviewsByMovieId(Integer movieId) {
        return movieDao.removeReviewsByMovieId(movieId);
    }

}
