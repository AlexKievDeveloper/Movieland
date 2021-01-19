package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.dao.jdbc.CachedGenreDao;
import com.hlushkov.movieland.dao.jdbc.JdbcGenreDao;
import com.hlushkov.movieland.entity.Genre;
import com.hlushkov.movieland.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultGenreService implements GenreService {
    private final CachedGenreDao cachedGenreDao;

    @Override
    public List<Genre> getAllGenres() {
        return cachedGenreDao.getAllGenres();
    }

}



















//List<Genre> cachedGenreList = jdbcGenreDao.getAllGenres();
// cachedGenreList.lastModified    compare with result of query SELECT pg_xact_commit_timestamp(xmin), * FROM  genres;
// FIXME Add check if table with genres was not modified from the last cachedGenreListModified




/*    @Override
    public String getGenreNameById(int genreId) {

        if (cachedGenreList.isEmpty()) {
            cachedGenreList = jdbcGenreDao.getAllGenres();
        }
        return cachedGenreList.get(genreId-1).getName();
    }*/
