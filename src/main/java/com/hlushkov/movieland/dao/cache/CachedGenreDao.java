package com.hlushkov.movieland.dao.cache;

import com.hlushkov.movieland.dao.GenreDao;
import com.hlushkov.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CachedGenreDao implements GenreDao {
    private final GenreDao jdbcGenreDao;
    private volatile List<Genre> cachedGenreList;

    @Override
    public List<Genre> findAll() {
        return new ArrayList<>(cachedGenreList);
    }

    @PostConstruct
    @Scheduled(initialDelayString = "${genre.cache.update.time}", fixedRateString = "${genre.cache.update.time}")
    private void updateCacheValues() {
        log.info("Refresh genre cache");
        cachedGenreList = jdbcGenreDao.findAll();
    }
}