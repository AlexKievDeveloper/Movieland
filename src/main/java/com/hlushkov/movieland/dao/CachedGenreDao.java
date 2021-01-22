package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.dao.jdbc.JdbcGenreDao;
import com.hlushkov.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
        //TODO Is it the best solution?
        Lock readLock = readWriteLock.readLock();
        Lock writeLock = readWriteLock.writeLock();

        if (cachedGenreList.isEmpty()) {
            /** Is it possible that many threads will stopped only here when the first thread will locked the object and
             continue their work from this place after first thread unlock object? */
            writeLock.lock();

            try {
                cachedGenreList = jdbcGenreDao.findAllGenres();
            } finally {
                writeLock.unlock();
            }
        }

        /**If there is other thread that is wirtingm current thread will be waiting before reading/returning till the
         * other thread finished writing and do writeLock.unlock();
           If other thread is reading, current thread would not be waiting for anything */
        try {
            readLock.lock();
            return cachedGenreList;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Synchronized is used for change cache in all cores after cached genre list changed
     */
    @Scheduled(fixedRateString = "${fixed.rate.in.milliseconds}")
    private synchronized void refreshCachedValues() {
        log.info("Scheduled method is running: {}", LocalDateTime.now());
        cachedGenreList = jdbcGenreDao.findAllGenres();
    }
}