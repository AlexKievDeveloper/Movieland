package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.entity.MovieRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Value("${random.movie.count}")
    private Long randomMovieCount;

    @Autowired
    private String getAllMovies;
    @Autowired
    private String getAllMoviesSortedByRating;
    @Autowired
    private String getAllMoviesSortedByDescPrice;
    @Autowired
    private String getAllMoviesSortedByAcsPrice;
    @Autowired
    private String getAllMoviesByGenreSortedByRating;
    @Autowired
    private String getAllMoviesByGenreSortedByDescPrice;
    @Autowired
    private String getAllMoviesByGenreSortedByAcsPrice;

    @Autowired
    private String getMoviesByGenre;

    //FIXME Разрулить этот трэш
    @Override
    public List<Movie> getAllMovies(MovieRequest movieRequest) {
        log.info("Request for all movies in dao level");
        if (movieRequest.getRatingDirection() != null) {
            if (movieRequest.getRatingDirection().getDirection().equals("desc")) {
                return jdbcTemplate.query(getAllMoviesSortedByRating, new MovieRowMapper());
            }
        } else if (movieRequest.getPriceDirection() != null) {
            if (movieRequest.getPriceDirection().getDirection().equals("desc")) {
                return jdbcTemplate.query(getAllMoviesSortedByDescPrice, new MovieRowMapper());
            } else {
                return jdbcTemplate.query(getAllMoviesSortedByAcsPrice, new MovieRowMapper());
            }
        }

        return jdbcTemplate.query(getAllMovies, new MovieRowMapper());
    }

    @Override
    public List<Movie> getRandomMovies() {
        log.info("Request for three random movies in dao level");
        List<Movie> allMoviesList = jdbcTemplate.query(getAllMovies, new MovieRowMapper());
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
    public List<Movie> getMoviesByGenre(int genreId, MovieRequest movieRequest) {
        log.info("Request for all movies by genre in dao level");


        if (movieRequest.getRatingDirection() != null) {
            if (movieRequest.getRatingDirection().getDirection().equals("desc")) {
                return jdbcTemplate.query(getAllMoviesByGenreSortedByRating, new MovieRowMapper(), genreId);
            }
        } else if (movieRequest.getPriceDirection() != null) {
            if (movieRequest.getPriceDirection().getDirection().equals("desc")) {
                return jdbcTemplate.query(getAllMoviesByGenreSortedByDescPrice, new MovieRowMapper(), genreId);
            } else {
                return jdbcTemplate.query(getAllMoviesByGenreSortedByAcsPrice, new MovieRowMapper(), genreId);
            }
        }


        return jdbcTemplate.query(getMoviesByGenre, new MovieRowMapper(), genreId);
    }
}
