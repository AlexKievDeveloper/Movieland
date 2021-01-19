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
    private final JdbcTemplate jdbcTemplate;
    private final String getAllGenres;

    @Override
    public List<Genre> getAllGenres() {
        log.info("Get request for all genres dao level");
        return jdbcTemplate.query(getAllGenres, new GenreRowMapper());
    }
}
