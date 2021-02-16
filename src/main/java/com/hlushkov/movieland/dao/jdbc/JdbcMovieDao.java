package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.hlushkov.movieland.dao.jdbc.mapper.MovieWithDetailsResultSetExtractor;
import com.hlushkov.movieland.dto.MovieWithDetails;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.request.MovieRequest;
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
    private final MovieWithDetailsResultSetExtractor movieWithDetailsResultSetExtractor = new MovieWithDetailsResultSetExtractor();
    private final JdbcTemplate jdbcTemplate;
    private final String findAllMovies;
    private final String findRandomMovies;
    private final String findMoviesByGenre;
    private final String findMovieWithDetailsByMovieId;
    @Value("${movie.random.count}")
    private Long randomMovieCount;

    @Override
    public List<Movie> findAll(MovieRequest movieRequest) {
        String query = generateQuery(findAllMovies, movieRequest);
        return jdbcTemplate.query(query, movieRowMapper);
    }

    @Override
    public List<Movie> findRandom() {
        return jdbcTemplate.query(findRandomMovies, movieRowMapper, randomMovieCount);
    }

    @Override
    public List<Movie> findByGenre(int genreId, MovieRequest movieRequest) {
        String query = generateQuery(findMoviesByGenre, movieRequest);
        return jdbcTemplate.query(query, movieRowMapper, genreId);
    }

    @Override
    public MovieWithDetails findMovieWithDetailsByMovieId(int movieId) {
        return jdbcTemplate.query(findMovieWithDetailsByMovieId, movieWithDetailsResultSetExtractor, movieId);
    }


    String generateQuery(String query, MovieRequest movieRequest) {
        String orderBy = " ORDER BY ";
        if (movieRequest.getRatingDirection().isPresent()) {
            if (movieRequest.getRatingDirection().get() == SortDirection.DESC) {
                return query + orderBy + "movie_rating " + SortDirection.DESC.getDirection();
            }
        } else if (movieRequest.getPriceDirection().isPresent()) {
            if (movieRequest.getPriceDirection().get() == SortDirection.DESC) {
                return query + orderBy + "movie_price " + SortDirection.DESC.getDirection();
            } else {
                return query + orderBy + "movie_price " + SortDirection.ASC.getDirection();
            }
        }

        return query;
    }
}
