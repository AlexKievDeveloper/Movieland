package com.hlushkov.movieland.dao.cache;

import com.hlushkov.movieland.common.request.FindMoviesRequest;
import com.hlushkov.movieland.common.request.SaveMovieRequest;
import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Repository
@Primary
public class CachedMovieDao implements MovieDao {
    private final MovieDao movieDao;
    private final Map<Integer, SoftReference<Movie>> cachedMovieMap = new ConcurrentHashMap<>();

    @Override
    public int saveMovie(SaveMovieRequest saveMovieRequest) {
        int movieId = movieDao.saveMovie(saveMovieRequest);
        Movie movie = movieDao.findById(movieId);
        cachedMovieMap.put(movieId, new SoftReference<>(movie));
        return movieId;
    }

    @Override
    public List<Movie> findMovies(FindMoviesRequest findMoviesRequest) {
        return movieDao.findMovies(findMoviesRequest);
    }

    @Override
    public List<Movie> findRandom() {
        return movieDao.findRandom();
    }

    @Override
    public List<Movie> findByGenre(int genreId, FindMoviesRequest findMoviesRequest) {
        return movieDao.findByGenre(genreId, findMoviesRequest);
    }

    @Override
    public Movie findById(int movieId) {
        if (cachedMovieMap.containsKey(movieId) && cachedMovieMap.get(movieId).get() != null) {
            log.info("Returning from cache movie with id: {}", movieId);
            return cachedMovieMap.get(movieId).get();
        } else {
            Movie movie = movieDao.findById(movieId);
            cachedMovieMap.put(movieId, new SoftReference<>(movie));
            log.info("Returning from database movie with id: {}", movieId);
            return movie.clone();
        }
    }

    @Override
    public void editMovie(Movie movie, Integer movieId) {
        movieDao.editMovie(movie, movieId);
        if (cachedMovieMap.containsKey(movie.getId())) {
            log.info("Updating movie with id: {} in movie cache", movie.getId());
            cachedMovieMap.put(movie.getId(), new SoftReference<>(movie));
        }
    }

    @Override
    public void editMovieGenres(Integer movieId, List<Integer> genreIds) {
        movieDao.editMovieGenres(movieId, genreIds);
    }

    @Override
    public void editMovieCountries(Integer movieId, List<Integer> countryIds) {
        movieDao.editMovieCountries(movieId, countryIds);
    }

    @Override
    public boolean removeReviewsByMovieId(Integer movieId) {
        return movieDao.removeReviewsByMovieId(movieId);
    }
}
