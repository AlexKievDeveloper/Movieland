package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.dao.CountryDao;
import com.hlushkov.movieland.dao.jdbc.mapper.CountryRowMapper;
import com.hlushkov.movieland.entity.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JdbcCountryDao implements CountryDao {

    private final CountryRowMapper countryRowMapper = new CountryRowMapper();
    private final JdbcTemplate jdbcTemplate;
    private final String addMoviesCountries;
    private final String findCountriesByMovieId;

    @Override
    public void saveMoviesCountries(List<Integer> countriesIds, int movieId) {
        jdbcTemplate.batchUpdate(
                addMoviesCountries,
                countriesIds,
                countriesIds.size(),
                (statementInLinks, countryId) -> {
                    statementInLinks.setInt(1, movieId);
                    statementInLinks.setInt(2, countryId);
                });
    }

    @Override
    public List<Country> findCountriesByMovieId(int movieId) {
        return jdbcTemplate.query(findCountriesByMovieId, countryRowMapper, movieId);
    }
}
