package com.hlushkov.movieland.service;

import com.hlushkov.movieland.entity.Genre;

import java.util.List;

public interface GenreService {

    void saveMoviesGenres(List<Integer> genresIds, int movieId);

    List<Genre> findAll();

    List<Genre> findByMovieId(int movieId);
}
