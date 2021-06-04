package com.hlushkov.movieland.service;

import com.hlushkov.movieland.entity.Genre;

import java.util.List;

public interface GenreService {

    List<Genre> findAll();

    List<Genre> findGenreByMovieId(int movieId);

}
