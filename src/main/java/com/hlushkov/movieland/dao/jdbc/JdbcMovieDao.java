package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.entity.MovieRequest;
import com.hlushkov.movieland.entity.SortDirection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcMovieDao implements MovieDao {
    private final MovieRowMapper movieRowMapper = new MovieRowMapper();
    private final JdbcTemplate jdbcTemplate;
    private final String findAllMovies;
    private final String findRandomMovies;
    private final String findMoviesByGenre;
    @Value("${random.movie.count}")
    private Long randomMovieCount;


    //FIXME optimize code block
    @Override
    public List<Movie> findAll(MovieRequest movieRequest) {
        log.info("Request for all movies in dao level");

        String query = generateQuery(findAllMovies, movieRequest);
        return jdbcTemplate.query(query, movieRowMapper);
    }

    @Override
    public List<Movie> findRandom() {
        log.info("Request for three random movies in dao level");
        return jdbcTemplate.query(findRandomMovies, movieRowMapper, randomMovieCount);
    }

    @Override
    public List<Movie> findByGenre(int genreId, MovieRequest movieRequest) {
        log.info("Request for all movies by genre in dao level");

        String query = generateQuery(findMoviesByGenre, movieRequest);
        return jdbcTemplate.query(query, movieRowMapper, genreId);
    }

    String generateQuery(String query, MovieRequest movieRequest) {
        if (movieRequest.getRatingDirection().isPresent()) {
            if (movieRequest.getRatingDirection().get() == SortDirection.DESC) {
                return query + " ORDER BY " + "rating " + SortDirection.DESC.getDirection();
            }
        } else if (movieRequest.getPriceDirection().isPresent()) {
            if (movieRequest.getPriceDirection().get() == SortDirection.DESC) {
                return query + " ORDER BY " + "price " + SortDirection.DESC.getDirection();
            } else {
                return query + " ORDER BY " + "price " + SortDirection.ASC.getDirection();
            }
        }

        return query;
    }
}
