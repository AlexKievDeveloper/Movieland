package com.hlushkov.movieland.service;

import com.hlushkov.movieland.entity.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> getAllMovies();

    List<Movie> getThreeRandomMovies();

    List<Movie> getMoviesByGenre(int genreId);
}

