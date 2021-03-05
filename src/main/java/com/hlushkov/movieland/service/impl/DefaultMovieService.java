package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.common.request.CreateUpdateMovieRequest;
import com.hlushkov.movieland.common.request.MovieRequest;
import com.hlushkov.movieland.dao.CountryDao;
import com.hlushkov.movieland.dao.GenreDao;
import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.dao.ReviewDao;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.CurrencyService;
import com.hlushkov.movieland.service.MovieService;
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
    private final CountryDao countryDao;
    private final GenreDao genreDao;
    private final ReviewDao reviewDao;
    private final CurrencyService currencyService;
    @Value("${thread.interrupting.period}")
    private Integer threadInterruptingPeriod;

    @Override
    public List<Movie> findMovies(MovieRequest movieRequest) {
        return movieDao.findMovies(movieRequest);
    }

    @Override
    public MovieDetails findMovieDetailsByMovieId(int movieId, Optional<String> requestedCurrency) {
        Movie movie = movieDao.findMovieById(movieId);
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
        threadPool.execute(() -> movieDetails.setGenres(genreDao.findByMovieId(movieId)));
        threadPool.execute(() -> movieDetails.setCountries(countryDao.findCountriesByMovieId(movieId)));
        threadPool.execute(() -> movieDetails.setReviews(reviewDao.findReviewsByMovieId(movieId)));

        log.debug("MovieDetails: {}", movieDetails);
        return movieDetails;
    }

    @Override
    public List<Movie> findRandom() {
        return movieDao.findRandomMovies();
    }

    @Override
    public List<Movie> findByGenre(int genreId, MovieRequest movieRequest) {
        return movieDao.findMoviesByGenre(genreId, movieRequest);
    }

    @Override
    public void modifyMovie(CreateUpdateMovieRequest createUpdateMovieRequest) {
        if (createUpdateMovieRequest.getId() != null) {
            movieDao.editMovie(createUpdateMovieRequest);
        } else {
            movieDao.addMovie(createUpdateMovieRequest);
        }
    }

}
