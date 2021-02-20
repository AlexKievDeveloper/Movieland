package com.hlushkov.movieland.service;

import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.common.request.MovieRequest;
import com.hlushkov.movieland.entity.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> findMovies(MovieRequest movieRequest);

    List<Movie> findRandom();

    List<Movie> findByGenre(int genreId, MovieRequest movieRequest);

    MovieDetails findMovieDetailsByMovieId(int movieId);

}

