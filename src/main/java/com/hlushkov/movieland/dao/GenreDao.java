package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.entity.Genre;

import java.util.List;

public interface GenreDao {

    void saveMoviesGenres(List<Integer> genresIds, int movieId);

    List<Genre> findAll();

    List<Genre> findByMovieId(int movieId);
}
