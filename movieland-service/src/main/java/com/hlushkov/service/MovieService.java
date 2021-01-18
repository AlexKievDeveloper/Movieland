package com.hlushkov.service;

import entity.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> getAllMovies();

    List<Movie> getThreeRandomMovies();

    List<Movie> getMoviesByGenre(int genreId);
}

