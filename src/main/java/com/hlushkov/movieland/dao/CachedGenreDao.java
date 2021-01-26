package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CachedGenreDao implements GenreDao {
    private final GenreDao jdbcGenreDao;
    private volatile List<Genre> cachedGenreList;

    @Override
    public List<Genre> findAll() {
        return cachedGenreList;
    }

    @PostConstruct
    @Scheduled(fixedRateString = "${fixed.rate.in.milliseconds}")
    private void refreshCachedValues() {
        log.info("Scheduled method is running: {}", LocalDateTime.now());
        cachedGenreList = jdbcGenreDao.findAll();
    }
}