package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.entity.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> findAll();
}
