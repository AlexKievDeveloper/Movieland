package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.dao.jdbc.JdbcGenreDao;
import com.hlushkov.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CachedGenreDao implements GenreDao {
    private final JdbcGenreDao jdbcGenreDao;
    private List<Genre> cachedGenreList = new ArrayList<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Override
    public List<Genre> findAllGenres() {
        Lock writeLock = readWriteLock.writeLock();//FIXME Is it the best solution?

        if (cachedGenreList.isEmpty()) {
            writeLock.lock();

            try {
                cachedGenreList = jdbcGenreDao.findAllGenres();
            } finally {
                writeLock.unlock();
            }
        }
        return cachedGenreList;
    }

    /**
     * synchronized is used for change cache in all cores after cached genre list changed
     */

    @Scheduled(fixedRateString = "${fixed.rate.in.milliseconds}")
    private synchronized void refreshCachedValues() {
        cachedGenreList = jdbcGenreDao.findAllGenres();
    }
}