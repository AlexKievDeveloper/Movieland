package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.entity.MovieRequest;

import java.util.List;

public interface MovieDao {

    List<Movie> getAllMovies(MovieRequest movieRequest);

    List<Movie> getThreeRandomMovies();

    List<Movie> getMoviesByGenre(int genreId);
}


