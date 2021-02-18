package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.dao.GenreDao;
import com.hlushkov.movieland.entity.Genre;
import com.hlushkov.movieland.service.GenreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DefaultGenreService implements GenreService {
    @Qualifier("cachedGenreDao")
    @Autowired
    private GenreDao genreDao;

    @Override
    public List<Genre> findAll() {
        return genreDao.findAll();
    }

}