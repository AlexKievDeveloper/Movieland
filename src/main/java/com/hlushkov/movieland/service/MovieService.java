package com.hlushkov.movieland.service;

import com.hlushkov.movieland.dto.MovieWithDetails;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.request.MovieRequest;

import java.util.List;

public interface MovieService {

    List<Movie> findAll(MovieRequest movieRequest);

    List<Movie> findRandom();

    List<Movie> findByGenre(int genreId, MovieRequest movieRequest);

    MovieWithDetails findMovieWithDetailsByMovieId(int movieId);
}

