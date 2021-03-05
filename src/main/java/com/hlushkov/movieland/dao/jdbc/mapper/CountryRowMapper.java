package com.hlushkov.movieland.dao.jdbc.mapper;

import com.hlushkov.movieland.entity.Country;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryRowMapper implements RowMapper<Country> {
    @Override
    public Country mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Country.builder()
                .id(resultSet.getInt("country_id"))
                .name(resultSet.getString("country_name"))
                .build();
    }
}
