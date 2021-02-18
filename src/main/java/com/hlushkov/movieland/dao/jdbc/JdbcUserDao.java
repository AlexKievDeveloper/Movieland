package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.dao.UserDao;
import com.hlushkov.movieland.dao.jdbc.mapper.UserRowMapper;
import com.hlushkov.movieland.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JdbcUserDao implements UserDao {
    private final UserRowMapper userRowMapper = new UserRowMapper();
    private final JdbcTemplate jdbcTemplate;
    private final String findUserByEmail;

    @Override
    public Optional<User> findByEmail(String userEmail) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(findUserByEmail, userRowMapper, userEmail));
    }
}
