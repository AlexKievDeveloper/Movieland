package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.common.request.AddMovieRequest;
import com.hlushkov.movieland.common.request.MovieRequest;
import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.dao.jdbc.extractor.MovieDetailsResultSetExtractor;
import com.hlushkov.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.hlushkov.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.StringJoiner;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JdbcMovieDao implements MovieDao {
    private final MovieRowMapper movieRowMapper = new MovieRowMapper();
    private final MovieDetailsResultSetExtractor movieDetailsResultSetExtractor = new MovieDetailsResultSetExtractor();
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final String findAllMovies;
    private final String findRandomMovies;
    private final String findMoviesByGenre;
    private final String findMovieWithDetailsByMovieId;
    private final String addMovie;
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
    public MovieDetails findMovieDetailsByMovieId(int movieId) {
        return jdbcTemplate.query(findMovieWithDetailsByMovieId, movieDetailsResultSetExtractor, movieId);
    }

    @Override
    public void addMovie(AddMovieRequest addMovieRequest) {
        MapSqlParameterSource parametersMap = getSqlParameterSource(addMovieRequest);
        String addMovieFullQuery = addMovie
                .concat(getRowWithCountryParameters(addMovieRequest.getCountriesIds()))
                .concat(" INSERT INTO movies_genres (movie_id, genre_id) VALUES ")
                .concat(getRowWithGenreParameters(addMovieRequest.getGenresIds()));

        namedParameterJdbcTemplate.update(addMovieFullQuery, parametersMap);
    }

    MapSqlParameterSource getSqlParameterSource(AddMovieRequest addMovieRequest) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("name_russian", addMovieRequest.getNameRussian());
        parameterSource.addValue("name_native", addMovieRequest.getNameNative());
        parameterSource.addValue("year_of_release", addMovieRequest.getYearOfRelease());
        parameterSource.addValue("description", addMovieRequest.getDescription());
        parameterSource.addValue("rating", addMovieRequest.getRating());
        parameterSource.addValue("price", addMovieRequest.getPrice());
        parameterSource.addValue("picture_path", addMovieRequest.getPicturePath());
        addMapSqlParametersForCountryIds(parameterSource, addMovieRequest.getCountriesIds());
        addMapSqlParametersForGenreIds(parameterSource, addMovieRequest.getGenresIds());
        return parameterSource;
    }

    void addMapSqlParametersForCountryIds(MapSqlParameterSource params, List<Integer> listCountryWithIds) {
        for (int i = 0; i < listCountryWithIds.size(); i++) {
            long countryId = listCountryWithIds.get(i);
            String paramName = "country_id" + i;
            params.addValue(paramName, countryId);
        }
    }

    void addMapSqlParametersForGenreIds(MapSqlParameterSource params, List<Integer> listWithGenreIds) {
        for (int i = 0; i < listWithGenreIds.size(); i++) {
            long countryId = listWithGenreIds.get(i);
            String paramName = "genre_id" + i;
            params.addValue(paramName, countryId);
        }
    }

    String getRowWithCountryParameters(List<Integer> listWithCountryIds) {
        MapSqlParameterSource parametersMap = new MapSqlParameterSource();
        addMapSqlParametersForCountryIds(parametersMap, listWithCountryIds);

        StringJoiner stringJoiner = new StringJoiner(", ", "", ")");
        String[] listParams = parametersMap.getParameterNames();

        for (String listParam : listParams) {
            stringJoiner.add("((SELECT movie_id_result FROM ins_movies), :" + listParam + ")");
        }
        return stringJoiner.toString();
    }

    String getRowWithGenreParameters(List<Integer> listWithGenreIds) {
        MapSqlParameterSource parametersMap = new MapSqlParameterSource();
        addMapSqlParametersForGenreIds(parametersMap, listWithGenreIds);

        StringJoiner stringJoiner = new StringJoiner(", ", "", "");
        String[] listParams = parametersMap.getParameterNames();

        for (String listParam : listParams) {
            stringJoiner.add("((SELECT movie_id_result FROM ins_movies), :" + listParam + ")");
        }
        return stringJoiner.toString();
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
