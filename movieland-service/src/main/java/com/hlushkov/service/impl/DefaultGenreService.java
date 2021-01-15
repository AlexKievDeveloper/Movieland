package com.hlushkov.service.impl;

import com.hlushkov.dao.jdbc.JdbcGenreDao;
import com.hlushkov.service.GenreService;
import entity.Genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultGenreService implements GenreService {
    private final JdbcGenreDao jdbcGenreDao;

    @Override
    public List<Genre> getAllGenres() {
        return jdbcGenreDao.getAllGenres();
    }
}
