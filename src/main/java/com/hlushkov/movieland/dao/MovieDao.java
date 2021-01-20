package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.entity.MovieRequest;

import java.util.List;

public interface MovieDao {

    List<Movie> findAllMovies(MovieRequest movieRequest);

    List<Movie> findRandomMovies();

    List<Movie> findMoviesByGenre(int genreId, MovieRequest movieRequest);
}


