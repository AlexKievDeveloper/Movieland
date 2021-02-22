package com.hlushkov.movieland.dao.jdbc.mapper;

import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .nickname(resultSet.getString("user_nickname"))
                .email(resultSet.getString("user_email"))
                .password(resultSet.getString("user_password"))
                .salt(resultSet.getString("user_salt"))
                .role(Role.getRole(resultSet.getString("user_role")))
                .build();
    }
}
