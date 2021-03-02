package com.hlushkov.movieland.service;

import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.common.request.CreateUpdateMovieRequest;
import com.hlushkov.movieland.common.request.MovieRequest;
import com.hlushkov.movieland.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    List<Movie> findMovies(MovieRequest movieRequest);

    List<Movie> findRandom();

    List<Movie> findByGenre(int genreId, MovieRequest movieRequest);

    MovieDetails findMovieDetailsByMovieId(int movieId, Optional<String> requestedCurrency);

    void modifyMovie(CreateUpdateMovieRequest createUpdateMovieRequest);
}

