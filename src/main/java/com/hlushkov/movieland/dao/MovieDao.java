package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.common.request.FindMoviesRequest;
import com.hlushkov.movieland.common.request.SaveMovieRequest;
import com.hlushkov.movieland.entity.Movie;

import java.util.List;

public interface MovieDao {

    void saveMovie(SaveMovieRequest saveMovieRequest);

    List<Movie> findMovies(FindMoviesRequest findMoviesRequest);

    List<Movie> findRandom();

    List<Movie> findByGenre(int genreId, FindMoviesRequest findMoviesRequest);

    Movie findById(int movieId);

    void editMovie(Movie movie);

    void editMovieGenres(Integer movieId, List<Integer> genreIds);

    void editMovieCountries(Integer movieId, List<Integer> countryIds);

    boolean removeReviewsByMovieId(Integer movieId);

}


