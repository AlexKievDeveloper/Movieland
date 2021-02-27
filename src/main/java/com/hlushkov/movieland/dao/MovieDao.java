package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.common.request.CreateUpdateMovieRequest;
import com.hlushkov.movieland.common.request.MovieRequest;
import com.hlushkov.movieland.entity.Movie;

import java.util.List;

public interface MovieDao {

    List<Movie> findMovies(MovieRequest movieRequest);

    List<Movie> findRandomMovies();

    List<Movie> findMoviesByGenre(int genreId, MovieRequest movieRequest);

    MovieDetails findMovieDetailsByMovieId(int movieId);

    void addMovie(CreateUpdateMovieRequest createUpdateMovieRequest);

    void editMovie(int movieId, CreateUpdateMovieRequest createUpdateMovieRequest);
}


