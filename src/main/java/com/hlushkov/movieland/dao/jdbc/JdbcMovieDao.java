package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.common.request.MovieRequest;
import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.dao.jdbc.extractor.MovieDetailsResultSetExtractor;
import com.hlushkov.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.hlushkov.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JdbcMovieDao implements MovieDao {
    private final MovieRowMapper movieRowMapper = new MovieRowMapper();
    private final MovieDetailsResultSetExtractor movieDetailsResultSetExtractor = new MovieDetailsResultSetExtractor();
    private final JdbcTemplate jdbcTemplate;
    private final String findAllMovies;
    private final String findRandomMovies;
    private final String findMoviesByGenre;
    private final String findMovieWithDetailsByMovieId;
    @Value("${movie.random.count}")
    private Long randomMovieCount;

    @Override
    public List<Movie> findMovies(MovieRequest movieRequest) {
        String generatedQueryForFindMovies = generateQuery(findAllMovies, movieRequest);
        return jdbcTemplate.query(generatedQueryForFindMovies, movieRowMapper);
    }

    @Override
    public List<Movie> findRandomMovies() {
        return jdbcTemplate.query(findRandomMovies, movieRowMapper, randomMovieCount);
    }

    @Override
    public List<Movie> findMoviesByGenre(int genreId, MovieRequest movieRequest) {
        String generatedQueryForFindMoviesByGenre = generateQuery(findMoviesByGenre, movieRequest);
        return jdbcTemplate.query(generatedQueryForFindMoviesByGenre, movieRowMapper, genreId);
    }

    @Override
    public MovieDetails findMovieDetailsByMovieId(int movieId) {
        return jdbcTemplate.query(findMovieWithDetailsByMovieId, movieDetailsResultSetExtractor, movieId);
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
