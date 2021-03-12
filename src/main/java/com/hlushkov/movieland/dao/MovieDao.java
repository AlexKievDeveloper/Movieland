package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.common.request.MovieRequest;
import com.hlushkov.movieland.entity.Movie;

import java.util.List;

public interface MovieDao {

    Integer saveMovie(Movie movie);

    List<Movie> findMovies(MovieRequest movieRequest);

    List<Movie> findRandom();

    List<Movie> findByGenre(int genreId, MovieRequest movieRequest);

    Movie findById(int movieId);

    void editMovie(Movie movie);

    void editMovieGenres(Integer movieId, List<Integer> genreIds);

    void editMovieCountries(Integer movieId, List<Integer> countryIds);

    boolean removeReviewsByMovieId(Integer movieId);

}


