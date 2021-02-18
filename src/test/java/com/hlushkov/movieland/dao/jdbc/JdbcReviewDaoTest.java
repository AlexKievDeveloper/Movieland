package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.config.TestWebContextConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestWebContextConfiguration
class JdbcReviewDaoTest {
    @Autowired
    private JdbcReviewDao jdbcReviewDao;

    @Test
    void getSqlParameterSource() {
        //when
        MapSqlParameterSource actualSourceMap = jdbcReviewDao.getSqlParameterSource(1, 1, "Nice text");
        //then
        assertTrue(actualSourceMap.hasValue("user_id"));
        assertTrue(actualSourceMap.hasValue("movie_id"));
        assertTrue(actualSourceMap.hasValue("text"));
        assertEquals(1, actualSourceMap.getValue("user_id"));
        assertEquals(1, actualSourceMap.getValue("movie_id"));
        assertEquals("Nice text", actualSourceMap.getValue("text"));
    }
}
