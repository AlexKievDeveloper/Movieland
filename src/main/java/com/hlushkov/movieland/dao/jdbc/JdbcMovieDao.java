package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.common.request.CreateUpdateMovieRequest;
import com.hlushkov.movieland.common.request.MovieRequest;
import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.hlushkov.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.StringJoiner;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcMovieDao implements MovieDao {
    private final MovieRowMapper movieRowMapper = new MovieRowMapper();

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final String findAllMovies;
    private final String findRandomMovies;
    private final String findMoviesByGenre;

    private final String addMovie;
    private final String editMovie;
    private final String removeMoviesCountries;
    private final String removeMoviesGenres;
    private final String addMoviesCountries;
    private final String addMoviesGenres;
    private final String constantMovieId = "movie_id";
    private final String findMovieById;

    @Value("${movie.random.count}")
    private Long randomMovieCount;


    @Override
    public List<Movie> findMovies(MovieRequest movieRequest) {
        String generatedQueryForFindMovies = generateQuery(findAllMovies, movieRequest);
        return jdbcTemplate.query(generatedQueryForFindMovies, movieRowMapper);
    }

    @Override
    public List<Movie> findRandomMovies() {
        return jdbcTemplate.query(findRandomMovies, movieRowMapper, randomMovieCount);
    }

    @Override
    public List<Movie> findMoviesByGenre(int genreId, MovieRequest movieRequest) {
        String generatedQueryForFindMoviesByGenre = generateQuery(findMoviesByGenre, movieRequest);
        return jdbcTemplate.query(generatedQueryForFindMoviesByGenre, movieRowMapper, genreId);
    }

    @Override
    public void addMovie(CreateUpdateMovieRequest createUpdateMovieRequest) {
        MapSqlParameterSource parametersMap = getSqlParameterSource(createUpdateMovieRequest);
        String addMovieFullQuery = addMovie
                .concat(getRowWithCountryParameters(createUpdateMovieRequest.getCountriesIds()))
                .concat(") INSERT INTO movies_genres (movie_id, genre_id) VALUES ")
                .concat(getRowWithGenreParameters(createUpdateMovieRequest.getGenresIds()));

        namedParameterJdbcTemplate.update(addMovieFullQuery, parametersMap);
    }

    @Transactional
    @Override
    public void editMovie(CreateUpdateMovieRequest createUpdateMovieRequest) {
        MapSqlParameterSource parametersMap = getSqlParameterSource(createUpdateMovieRequest);
        parametersMap.addValue(constantMovieId, createUpdateMovieRequest.getId());

        if (createUpdateMovieRequest.getCountriesIds() != null) {
            removeMoviesCountriesByMovieId(createUpdateMovieRequest.getId());
            addMoviesCountries(parametersMap, createUpdateMovieRequest.getCountriesIds());
        }

        if (createUpdateMovieRequest.getGenresIds() != null) {
            removeMoviesGenresByMovieId(createUpdateMovieRequest.getId());
            addMoviesGenres(parametersMap, createUpdateMovieRequest.getGenresIds());
        }

        namedParameterJdbcTemplate.update(editMovie, parametersMap);
    }

    @Override
    public Movie findMovieById(int movieId) {
        return jdbcTemplate.queryForObject(findMovieById, movieRowMapper, movieId);
    }

    MapSqlParameterSource getSqlParameterSource(CreateUpdateMovieRequest createUpdateMovieRequest) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("name_russian", createUpdateMovieRequest.getNameRussian());
        parameterSource.addValue("name_native", createUpdateMovieRequest.getNameNative());
        parameterSource.addValue("year_of_release", createUpdateMovieRequest.getYearOfRelease());
        parameterSource.addValue("description", createUpdateMovieRequest.getDescription());
        parameterSource.addValue("rating", createUpdateMovieRequest.getRating());
        parameterSource.addValue("price", createUpdateMovieRequest.getPrice());
        parameterSource.addValue("picture_path", createUpdateMovieRequest.getPicturePath());
        addMapSqlParametersForCountryIds(parameterSource, createUpdateMovieRequest.getCountriesIds());
        addMapSqlParametersForGenreIds(parameterSource, createUpdateMovieRequest.getGenresIds());
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

    String getRowWithCountryParameters(List<Integer> listWithCountryIds) {
        MapSqlParameterSource parametersMap = new MapSqlParameterSource();
        addMapSqlParametersForCountryIds(parametersMap, listWithCountryIds);
        return getRowWithParameters(parametersMap);
    }

    String getRowWithGenreParameters(List<Integer> listWithGenreIds) {
        MapSqlParameterSource parametersMap = new MapSqlParameterSource();
        addMapSqlParametersForGenreIds(parametersMap, listWithGenreIds);
        return getRowWithParameters(parametersMap);
    }

    String getRowWithParameters(MapSqlParameterSource parametersMap) {
        StringJoiner stringJoiner = new StringJoiner(", ", "", "");
        String[] listParams = parametersMap.getParameterNames();

        for (String listParam : listParams) {
            stringJoiner.add("((SELECT movie_id_result FROM ins_movies), :" + listParam + ")");
        }
        return stringJoiner.toString();
    }

    void addMoviesCountries(MapSqlParameterSource parametersMap, List<Integer> countriesIds) {
        addMapSqlParametersForCountryIds(parametersMap, countriesIds);
        String addMoviesCountriesFullQuery = generateAddMoviesCountriesFullQuery(addMoviesCountries, countriesIds);
        namedParameterJdbcTemplate.update(addMoviesCountriesFullQuery, parametersMap);
    }

    void addMoviesGenres(MapSqlParameterSource parametersMap, List<Integer> genresIds) {
        addMapSqlParametersForGenreIds(parametersMap, genresIds);
        String addMoviesGenresFullQuery = generateAddMoviesGenresFullQuery(addMoviesGenres, genresIds);
        namedParameterJdbcTemplate.update(addMoviesGenresFullQuery, parametersMap);
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

    String generateAddMoviesCountriesFullQuery(String addMoviesCountries, List<Integer> countriesIds) {
        MapSqlParameterSource parametersMap = new MapSqlParameterSource();
        addMapSqlParametersForCountryIds(parametersMap, countriesIds);

        StringJoiner stringJoiner = new StringJoiner(", ", "", "");
        String[] listParams = parametersMap.getParameterNames();

        for (String listParam : listParams) {
            stringJoiner.add("(:movie_id, :" + listParam + ")");
        }
        return addMoviesCountries + stringJoiner.toString();
    }

    String generateAddMoviesGenresFullQuery(String addMoviesGenres, List<Integer> genresIds) {
        MapSqlParameterSource parametersMap = new MapSqlParameterSource();
        addMapSqlParametersForGenreIds(parametersMap, genresIds);

        StringJoiner stringJoiner = new StringJoiner(", ", "", "");
        String[] listParams = parametersMap.getParameterNames();

        for (String listParam : listParams) {
            stringJoiner.add("(:movie_id, :" + listParam + ")");
        }
        return addMoviesGenres + stringJoiner.toString();
    }

    String generateQuery(String query, MovieRequest movieRequest) {
        String orderBy = " ORDER BY ";
        if (movieRequest.getRatingDirection().isPresent()) {
            if (movieRequest.getRatingDirection().get() == SortDirection.DESC) {
                return query + orderBy + "movie_rating " + SortDirection.DESC.getDirection();
            }
        } else if (movieRequest.getPriceDirection().isPresent()) {
            if (movieRequest.getPriceDirection().get() == SortDirection.DESC) {
                return query + orderBy + "movie_price " + SortDirection.DESC.getDirection();
            } else {
                return query + orderBy + "movie_price " + SortDirection.ASC.getDirection();
            }
        }

        return query;
    }

}
