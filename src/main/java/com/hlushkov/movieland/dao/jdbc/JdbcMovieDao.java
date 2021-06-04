package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.common.request.FindMoviesRequest;
import com.hlushkov.movieland.common.request.SaveMovieRequest;
import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.hlushkov.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcMovieDao implements MovieDao {
    private final MovieRowMapper movieRowMapper = new MovieRowMapper();
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final String constantMovieId = "movie_id";
    private final String findAllMovies;
    private final String findRandomMovies;
    private final String findMoviesByGenre;
    private final String saveMovie;
    private final String editMovie;
    private final String removeMoviesCountries;
    private final String removeMoviesGenres;
    private final String addMoviesCountries;
    private final String addMoviesGenres;
    private final String findMovieById;
    private final String removeReviewsByMovieId;

    @Value("${movie.random.count}")
    private Long randomMovieCount;

    @Transactional
    @Override
    public int saveMovie(SaveMovieRequest saveMovieRequest) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(saveMovie, new String[]{constantMovieId});
            statement.setString(1, saveMovieRequest.getNameRussian());
            statement.setString(2, saveMovieRequest.getNameNative());
            statement.setInt(3, saveMovieRequest.getYearOfRelease());
            statement.setString(4, saveMovieRequest.getDescription());
            statement.setDouble(5, saveMovieRequest.getRating());
            statement.setDouble(6, saveMovieRequest.getPrice());
            statement.setString(7, saveMovieRequest.getPicturePath());
            return statement;
        }, keyHolder);

        Integer movieId = (Integer) Objects.requireNonNull(keyHolder.getKey());
        addMoviesCountries(movieId, saveMovieRequest.getCountriesIds());
        addMoviesGenres(movieId, saveMovieRequest.getGenresIds());
        return movieId;
    }

    @Override
    public List<Movie> findMovies(FindMoviesRequest findMoviesRequest) {
        String generatedQueryForFindMovies = generateQuery(findAllMovies, findMoviesRequest);
        return jdbcTemplate.query(generatedQueryForFindMovies, movieRowMapper);
    }

    @Override
    public List<Movie> findRandom() {
        return jdbcTemplate.query(findRandomMovies, movieRowMapper, randomMovieCount);
    }

    @Override
    public List<Movie> findByGenre(int genreId, FindMoviesRequest findMoviesRequest) {
        String generatedQueryForFindMoviesByGenre = generateQuery(findMoviesByGenre, findMoviesRequest);
        return jdbcTemplate.query(generatedQueryForFindMoviesByGenre, movieRowMapper, genreId);
    }

    @Override
    public Movie findById(int movieId) {
        log.info("Request for find movie by id from database, id {}", movieId);
        return jdbcTemplate.queryForObject(findMovieById, movieRowMapper, movieId);
    }

    @Override
    public void editMovie(Movie movie, Integer movieId) {
        MapSqlParameterSource parametersMap = getSqlParameterSource(movie);
        parametersMap.addValue(constantMovieId, movieId);
        namedParameterJdbcTemplate.update(editMovie, parametersMap);
    }

    @Override
    public void editMovieGenres(Integer movieId, List<Integer> genreIds) {
        removeMoviesGenresByMovieId(movieId);
        addMoviesGenres(movieId, genreIds);
    }

    @Override
    public void editMovieCountries(Integer movieId, List<Integer> countryIds) {
        removeMoviesCountriesByMovieId(movieId);
        addMoviesCountries(movieId, countryIds);
    }

    @Override
    public boolean removeReviewsByMovieId(Integer movieId) {
        MapSqlParameterSource parametersMap = new MapSqlParameterSource();
        parametersMap.addValue(constantMovieId, movieId);
        return namedParameterJdbcTemplate.update(removeReviewsByMovieId, parametersMap) != 0;
    }

    MapSqlParameterSource getSqlParameterSource(Movie movie) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("name_russian", movie.getNameRussian());
        parameterSource.addValue("name_native", movie.getNameNative());
        parameterSource.addValue("year_of_release", movie.getYearOfRelease());
        parameterSource.addValue("description", movie.getDescription());
        parameterSource.addValue("rating", movie.getRating());
        parameterSource.addValue("price", movie.getPrice());
        parameterSource.addValue("picture_path", movie.getPicturePath());
        return parameterSource;
    }

    void addMapSqlParametersForCountryIds(MapSqlParameterSource params, List<Integer> listCountryWithIds) {
        if (listCountryWithIds != null) {
            for (int i = 0; i < listCountryWithIds.size(); i++) {
                long countryId = listCountryWithIds.get(i);
                String paramName = "country_id" + i;
                params.addValue(paramName, countryId);
            }
        }
    }

    void addMapSqlParametersForGenreIds(MapSqlParameterSource params, List<Integer> listWithGenreIds) {
        if (listWithGenreIds != null) {
            for (int i = 0; i < listWithGenreIds.size(); i++) {
                long countryId = listWithGenreIds.get(i);
                String paramName = "genre_id" + i;
                params.addValue(paramName, countryId);
            }
        }
    }

    void addMoviesCountries(Integer movieId, List<Integer> countryIds) {
        jdbcTemplate.batchUpdate(
                addMoviesCountries,
                countryIds,
                countryIds.size(),
                (statementInLinks, countryId) -> {
                    statementInLinks.setInt(1, movieId);
                    statementInLinks.setInt(2, countryId);
                });
    }

    void addMoviesGenres(Integer movieId, List<Integer> genreIds) {
        jdbcTemplate.batchUpdate(
                addMoviesGenres,
                genreIds,
                genreIds.size(),
                (statementInLinks, genreId) -> {
                    statementInLinks.setInt(1, movieId);
                    statementInLinks.setInt(2, genreId);
                });
    }

    void removeMoviesCountriesByMovieId(int movieId) {
        MapSqlParameterSource parametersMap = new MapSqlParameterSource();
        parametersMap.addValue(constantMovieId, movieId);
        namedParameterJdbcTemplate.update(removeMoviesCountries, parametersMap);
    }

    void removeMoviesGenresByMovieId(int movieId) {
        MapSqlParameterSource parametersMap = new MapSqlParameterSource();
        parametersMap.addValue(constantMovieId, movieId);
        namedParameterJdbcTemplate.update(removeMoviesGenres, parametersMap);
    }

    String generateQuery(String query, FindMoviesRequest findMoviesRequest) {
        String orderBy = " ORDER BY ";
        if (findMoviesRequest.getRatingDirection().isPresent()) {
            if (findMoviesRequest.getRatingDirection().get() == SortDirection.DESC) {
                return query + orderBy + "movie_rating " + SortDirection.DESC.getDirection();
            }
        } else if (findMoviesRequest.getPriceDirection().isPresent()) {
            if (findMoviesRequest.getPriceDirection().get() == SortDirection.DESC) {
                return query + orderBy + "movie_price " + SortDirection.DESC.getDirection();
            } else {
                return query + orderBy + "movie_price " + SortDirection.ASC.getDirection();
            }
        }

        return query;
    }

}
