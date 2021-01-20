package com.hlushkov.movieland.service;

import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.entity.MovieRequest;

import java.util.List;

public interface MovieService {

    List<Movie> getAllMovies(MovieRequest movieRequest);

    List<Movie> getThreeRandomMovies();

    List<Movie> getMoviesByGenre(int genreId);
}

