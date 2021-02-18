package com.hlushkov.movieland.dao.jdbc.mapper;

import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRowMapperTest {
    @Mock
    private ResultSet resultSet;
    private UserRowMapper rowMapper;

    public UserRowMapperTest() {
        rowMapper = new UserRowMapper();
    }

    @Test
    @DisplayName("Returns User from result set")
    void mapRow() throws SQLException {
        //prepare
        when(resultSet.getInt("user_id")).thenReturn(1);
        when(resultSet.getString("user_nickname")).thenReturn("Рональд Рейнольдс");
        when(resultSet.getString("user_email")).thenReturn("ronald@gmail.com");
        when(resultSet.getString("user_password")).thenReturn("password");
        when(resultSet.getString("user_salt")).thenReturn("salt");
        when(resultSet.getString("user_role")).thenReturn("user");
        //when
        User actualUser = rowMapper.mapRow(resultSet, 0);
        //then
        assertEquals(1, actualUser.getId());
        assertEquals("Рональд Рейнольдс", actualUser.getNickname());
        assertEquals("ronald@gmail.com", actualUser.getEmail());
        assertEquals("password", actualUser.getPassword());
        assertEquals("salt", actualUser.getSalt());
        assertEquals(Role.USER, actualUser.getRole());
    }
}