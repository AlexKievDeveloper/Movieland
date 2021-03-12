package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.common.exception.NoUserFoundException;
import com.hlushkov.movieland.dao.UserDao;
import com.hlushkov.movieland.dao.jdbc.mapper.UserRowMapper;
import com.hlushkov.movieland.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JdbcUserDao implements UserDao {
    private final UserRowMapper userRowMapper = new UserRowMapper();
    private final JdbcTemplate jdbcTemplate;
    private final String findUserByEmail;

    @Override
    public User findByEmail(String userEmail) {
        try {
            return jdbcTemplate.queryForObject(findUserByEmail, userRowMapper, userEmail);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NoUserFoundException("Exception while getting user by email from db: " + userEmail, e);
        }
    }
}
