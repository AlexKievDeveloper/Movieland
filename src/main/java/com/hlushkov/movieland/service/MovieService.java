package com.hlushkov.movieland.service;

import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.entity.MovieRequest;

import java.util.List;

public interface MovieService {

    List<Movie> findAllMovies(MovieRequest movieRequest);

    List<Movie> findRandomMovies();

    List<Movie> findMoviesByGenre(int genreId, MovieRequest movieRequest);
}

