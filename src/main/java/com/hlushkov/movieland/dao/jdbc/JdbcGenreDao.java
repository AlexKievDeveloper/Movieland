package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.dao.GenreDao;
import com.hlushkov.movieland.dao.jdbc.mapper.GenreRowMapper;
import com.hlushkov.movieland.entity.Genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcGenreDao implements GenreDao {
    private final GenreRowMapper genreRowMapper = new GenreRowMapper();
    private final JdbcTemplate jdbcTemplate;
    private final String findAllGenres;

    @Override
    public List<Genre> findAll() {
        log.info("Get request for all genres dao level");
        return jdbcTemplate.query(findAllGenres, genreRowMapper);
    }

}
