package com.hlushkov.movieland.dao.cache;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

import com.hlushkov.movieland.dao.CachedDao;
import com.hlushkov.movieland.dao.GenreDao;
import com.hlushkov.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class DefaultCachedDao implements CachedDao {
    private final GenreDao jdbcGenreDao;
    private volatile List<Genre> cachedGenreList;

    @Override
    public List<Genre> findAllGenres() {
        log.info("Request for find all genre in cached genre dao level");
        log.info("CACHED GENRE LIST: {}", cachedGenreList);
        return cachedGenreList;
    }

    @PostConstruct
    @Scheduled(initialDelayString = "${fixed.rate.in.milliseconds}", fixedRateString = "${fixed.rate.in.milliseconds}")
    private void updateCacheValues() {
        log.info("Scheduled method is running: {}", LocalDateTime.now());
        cachedGenreList = jdbcGenreDao.findAll();
    }
}