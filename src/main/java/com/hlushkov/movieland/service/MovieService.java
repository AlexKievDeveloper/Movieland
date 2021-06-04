package com.hlushkov.movieland.service;

import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.common.request.FindMoviesRequest;
import com.hlushkov.movieland.common.request.SaveMovieRequest;
import com.hlushkov.movieland.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    void saveMovie(SaveMovieRequest saveMovieRequest);

    List<Movie> findMovies(FindMoviesRequest findMoviesRequest);

    List<Movie> findRandom();

    List<Movie> findByGenre(int genreId, FindMoviesRequest findMoviesRequest);

    MovieDetails findById(int movieId, Optional<String> requestedCurrency);

    void editMovie(Movie movie, Integer movieId);

    void editMovieGenres(Integer movieId, List<Integer> genreIds);

    void editMovieCountries(Integer movieId, List<Integer> countryIds);

    boolean removeReviewsByMovieId(Integer movieId);

}

