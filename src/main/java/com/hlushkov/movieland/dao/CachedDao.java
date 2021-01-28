package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.entity.Genre;

import java.util.List;

public interface CachedDao {
    List<Genre> findAllGenres();
}
