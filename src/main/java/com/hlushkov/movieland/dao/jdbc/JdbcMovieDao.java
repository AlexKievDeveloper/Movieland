package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.entity.MovieRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcMovieDao implements MovieDao {
    private final JdbcTemplate jdbcTemplate;
    private final String findAllMovies;
    private final String findAllMoviesSortedByRating;
    private final String findAllMoviesSortedByDescPrice;
    private final String findAllMoviesSortedByAcsPrice;
    private final String findAllMoviesByGenreSortedByRating;
    private final String findAllMoviesByGenreSortedByDescPrice;
    private final String findAllMoviesByGenreSortedByAcsPrice;
    private final String findMoviesByGenre;
    @Value("${random.movie.count}")
    private Long randomMovieCount;

    //FIXME Разрулить этот трэш
    @Override
    public List<Movie> findAllMovies(MovieRequest movieRequest) {
        log.info("Request for all movies in dao level");

        if (movieRequest.getRatingDirection() != null) {
            if (movieRequest.getRatingDirection().getDirection().equals("desc")) {
                return jdbcTemplate.query(findAllMoviesSortedByRating, new MovieRowMapper());
            }
        } else if (movieRequest.getPriceDirection() != null) {
            if (movieRequest.getPriceDirection().getDirection().equals("desc")) {
                return jdbcTemplate.query(findAllMoviesSortedByDescPrice, new MovieRowMapper());
            } else {
                return jdbcTemplate.query(findAllMoviesSortedByAcsPrice, new MovieRowMapper());
            }
        }
        return jdbcTemplate.query(findAllMovies, new MovieRowMapper());
    }

    @Override
    public List<Movie> findRandomMovies() {
        log.info("Request for three random movies in dao level");
        List<Movie> allMoviesList = jdbcTemplate.query(findAllMovies, new MovieRowMapper());
        List<Movie> randomMovieList = new ArrayList<>();

        if (randomMovieCount <= allMoviesList.size()) {
            for (int i = 0; i < randomMovieCount; i++) {
                int randomNumber = ThreadLocalRandom.current().nextInt(allMoviesList.size());
                randomMovieList.add(allMoviesList.get(randomNumber));
                allMoviesList.remove(randomNumber);
            }
        }

        return randomMovieList;
    }

    //FIXME Разрулить этот трэш
    @Override
    public List<Movie> findMoviesByGenre(int genreId, MovieRequest movieRequest) {
        log.info("Request for all movies by genre in dao level");

        if (movieRequest.getRatingDirection() != null) {
            if (movieRequest.getRatingDirection().getDirection().equals("desc")) {
                return jdbcTemplate.query(findAllMoviesByGenreSortedByRating, new MovieRowMapper(), genreId);
            }
        } else if (movieRequest.getPriceDirection() != null) {
            if (movieRequest.getPriceDirection().getDirection().equals("desc")) {
                return jdbcTemplate.query(findAllMoviesByGenreSortedByDescPrice, new MovieRowMapper(), genreId);
            } else {
                return jdbcTemplate.query(findAllMoviesByGenreSortedByAcsPrice, new MovieRowMapper(), genreId);
            }
        }

        return jdbcTemplate.query(findMoviesByGenre, new MovieRowMapper(), genreId);
    }
}
