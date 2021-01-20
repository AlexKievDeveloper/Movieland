package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.dao.CachedGenreDao;
import com.hlushkov.movieland.entity.Genre;
import com.hlushkov.movieland.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultGenreService implements GenreService {
    private final CachedGenreDao cachedGenreDao;

    @Override
    public List<Genre> findAllGenres() {
        log.info("Request for find all genre is service level");
        return cachedGenreDao.findAllGenres();
    }
}