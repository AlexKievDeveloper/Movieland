package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.entity.Movie;

import java.util.List;

public interface MovieDao {

    List<Movie> getAllMovies();

    List<Movie> getThreeRandomMovies();

    List<Movie> getMoviesByGenre(int genreId);
}


