package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.common.request.CreateUpdateMovieRequest;
import com.hlushkov.movieland.common.request.MovieRequest;
import com.hlushkov.movieland.entity.Movie;

import java.util.List;

public interface MovieDao {

    List<Movie> findMovies(MovieRequest movieRequest);

    List<Movie> findRandomMovies();

    List<Movie> findMoviesByGenre(int genreId, MovieRequest movieRequest);

    void addMovie(CreateUpdateMovieRequest createUpdateMovieRequest);

    void editMovie(CreateUpdateMovieRequest createUpdateMovieRequest);

    Movie findMovieById(int movieId);
}


