package com.hlushkov.dao;

import entity.Movie;

import java.util.List;

public interface MovieDao {

    List<Movie> getAllMovies();

    List<Movie> getThreeRandomMovies();

    List<Movie> getMoviesByGenre(int genreId);
}

