package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.dao.GenreDao;
import com.hlushkov.movieland.dao.jdbc.mapper.GenreRowMapper;
import com.hlushkov.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JdbcGenreDao implements GenreDao {
    private final GenreRowMapper genreRowMapper = new GenreRowMapper();
    private final JdbcTemplate jdbcTemplate;
    private final String addMoviesGenres;
    private final String findAllGenres;
    private final String findGenresByMovieId;

    @Override
    public void saveMoviesGenres(List<Integer> genresIds, int movieId) {
        jdbcTemplate.batchUpdate(
                addMoviesGenres,
                genresIds,
                genresIds.size(),
                (statementInLinks, genreId) -> {
                    statementInLinks.setInt(1, movieId);
                    statementInLinks.setInt(2, genreId);
                });
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query(findAllGenres, genreRowMapper);
    }

    @Override
    public List<Genre> findByMovieId(int movieId) {
        return jdbcTemplate.query(findGenresByMovieId, genreRowMapper, movieId);
    }

}
