package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.dao.GenreDao;
import com.hlushkov.movieland.entity.Genre;
import com.hlushkov.movieland.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultGenreService implements GenreService {
    private final GenreDao genreDao;

    @Override
    public void saveMoviesGenres(List<Integer> genresIds, int movieId) {
        genreDao.saveMoviesGenres(genresIds, movieId);
    }

    @Override
    public List<Genre> findAll() {
        return genreDao.findAll();
    }

    @Override
    public List<Genre> findByMovieId(int movieId) {
        return genreDao.findByMovieId(movieId);
    }

}