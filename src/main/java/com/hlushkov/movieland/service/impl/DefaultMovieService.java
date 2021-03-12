package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.common.request.FindMoviesRequest;
import com.hlushkov.movieland.common.request.SaveMovieRequest;
import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.*;
import com.hlushkov.movieland.service.util.TimeoutedThreadPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;
    private final CountryService countryService;
    private final GenreService genreService;
    private final ReviewService reviewService;
    private final CurrencyService currencyService;
    @Value("${thread.interrupting.period}")
    private Integer threadInterruptingPeriod;

    @Override
    public void saveMovie(SaveMovieRequest saveMovieRequest) {
        movieDao.saveMovie(saveMovieRequest);
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
    public MovieDetails findById(int movieId, Optional<String> requestedCurrency) {
        Movie movie = movieDao.findById(movieId);
        requestedCurrency.ifPresent(currency -> movie.setPrice(currencyService.convert(movie.getPrice(), currency)));
        MovieDetails movieDetails = MovieDetails.builder()
                .id(movie.getId())
                .nameNative(movie.getNameNative())
                .nameRussian(movie.getNameRussian())
                .yearOfRelease(movie.getYearOfRelease())
                .description(movie.getDescription())
                .price(movie.getPrice())
                .rating(movie.getRating())
                .picturePath(movie.getPicturePath()).build();


        TimeoutedThreadPool threadPool = new TimeoutedThreadPool(threadInterruptingPeriod, 1, 10, 5, TimeUnit.MINUTES, new SynchronousQueue());
        threadPool.execute(() -> movieDetails.setGenres(genreService.findByMovieId(movieId)));
        threadPool.execute(() -> movieDetails.setCountries(countryService.findCountriesByMovieId(movieId)));
        threadPool.execute(() -> movieDetails.setReviews(reviewService.findReviewsByMovieId(movieId)));

        log.debug("MovieDetails: {}", movieDetails);
        return movieDetails;
    }

    @Override
    public void editMovie(Movie movie) {
        movieDao.editMovie(movie);
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
