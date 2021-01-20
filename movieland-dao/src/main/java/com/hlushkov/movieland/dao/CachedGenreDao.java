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
    public List<Genre> getAllGenres() {
        Lock writeLock = readWriteLock.writeLock();

        if (cachedGenreList.isEmpty()) {
            writeLock.lock();

            try {
                cachedGenreList = jdbcGenreDao.getAllGenres();
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
        cachedGenreList = jdbcGenreDao.getAllGenres();
    }
}


/**
 * 1) synchronized блок в методе:
 * synchronized(jdbcGenreDao) {
 * cachedGenreList = jdbcGenreDao.getAllGenres();
 * }
 * Все 100 потоков захватят CachedGenreDao и вызовут на нём getAllGenres() но доступ к jdbcGenreDao получат поочерёдно.
 * Но тем не менее все 100 потоков произведут вызов jdbcGenreDao.getAllGenres(); А мне такого не нужно.
 * Мне нужно чтобы доступ к вызову метода getAllGenres() на объекте jdbcGenreDao получил только первый поток,
 * и после отрабатывания метода остальные потоки при вызове getAllGenres() на CachedGenreDao сразу получили
 * лист с кэшем без похода в БД:
 * <p>
 * 2)      @Override
 * public synchronized List<Genre> getAllGenres() {
 * if (cachedGenreList.isEmpty()) {
 * cachedGenreList = jdbcGenreDao.getAllGenres();
 * }
 * return cachedGenreList;
 * }
 * <p>
 * Но это также помешает в будущем множеству потоков одновременно захватить объект CachedGenreDao и вызвать на нём
 * getAllGenres(), что плохо потому что кэш уже будет в листе и так как он только вычитывается и не изменяется то нет
 * необходимости ограничивать одновременный доступ к нему
 * <p>
 * 3) При writeLock.lock(); не будет одновременного доступа потоков к записи, но тем не менее все потоки что то запишут
 */