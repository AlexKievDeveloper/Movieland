package com.hlushkov.movieland.dao.cache;

import com.hlushkov.movieland.dao.GenreDao;
import com.hlushkov.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
@Primary
public class CachedGenreDao implements GenreDao {
    private final GenreDao genreDao;
    private volatile List<Genre> cachedGenreList;

    @Override
    public void saveMoviesGenres(List<Integer> genresIds, int movieId) {
        genreDao.saveMoviesGenres(genresIds, movieId);
    }

    @Override
    public List<Genre> findAll() {
        return new ArrayList<>(cachedGenreList);
    }

    @Override
    public List<Genre> findByMovieId(int movieId) {
        return genreDao.findByMovieId(movieId);
    }

    @PostConstruct
    @Scheduled(initialDelayString = "${genre.cache.update.time.interval}", fixedRateString = "${genre.cache.update.time.interval}")
    private void updateCacheValues() {
        log.info("Refresh genre cache");
        cachedGenreList = genreDao.findAll();
    }
}