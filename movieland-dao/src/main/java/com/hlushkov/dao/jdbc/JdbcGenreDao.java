package com.hlushkov.dao.jdbc;

import com.hlushkov.dao.GenreDao;
import com.hlushkov.dao.jdbc.mapper.GenreRowMapper;
import entity.Genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcGenreDao implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private String getAllGenres;

    @Override
    public List<Genre> getAllGenres() {
        log.info("Get request for all genres dao level");
        return jdbcTemplate.query(getAllGenres, new GenreRowMapper());
    }
}
