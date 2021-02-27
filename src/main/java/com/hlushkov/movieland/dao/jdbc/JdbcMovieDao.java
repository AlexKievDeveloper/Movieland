package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.common.request.CreateUpdateMovieRequest;
import com.hlushkov.movieland.common.request.MovieRequest;
import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.dao.jdbc.extractor.MovieDetailsResultSetExtractor;
import com.hlushkov.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.hlushkov.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.StringJoiner;

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
    private final String editMovie;
    private final String removeMoviesCountries;
    private final String removeMoviesGenres;
    private final String addMoviesCountries;
    private final String addMoviesGenres;
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
    public void addMovie(CreateUpdateMovieRequest createUpdateMovieRequest) {
        MapSqlParameterSource parametersMap = getSqlParameterSource(createUpdateMovieRequest);
        String addMovieFullQuery = addMovie
                .concat(getRowWithCountryParameters(createUpdateMovieRequest.getCountriesIds()))
                .concat(" INSERT INTO movies_genres (movie_id, genre_id) VALUES ")
                .concat(getRowWithGenreParameters(createUpdateMovieRequest.getGenresIds()));

        namedParameterJdbcTemplate.update(addMovieFullQuery, parametersMap);
    }

    @Transactional
    @Override
    public void editMovie(int movieId, CreateUpdateMovieRequest createUpdateMovieRequest) {
        MapSqlParameterSource parametersMap = getSqlParameterSourceForEditMovie(movieId, createUpdateMovieRequest);
        String editMovieFullQuery = generateFullEditMovieQuery(editMovie, createUpdateMovieRequest);

        if (createUpdateMovieRequest.getCountriesIds() != null) {
            removeMoviesCountriesByMovieId(movieId);
            addMoviesCountries(parametersMap, createUpdateMovieRequest.getCountriesIds());
        }

        if (createUpdateMovieRequest.getGenresIds() != null) {
            removeMoviesGenresByMovieId(movieId);
            addMoviesGenres(parametersMap, createUpdateMovieRequest.getGenresIds());
        }

        namedParameterJdbcTemplate.update(editMovieFullQuery, parametersMap);
    }

    String generateFullEditMovieQuery(String editMovie, CreateUpdateMovieRequest createUpdateMovieRequest) {
        StringJoiner stringJoiner = new StringJoiner("");
        stringJoiner.add(editMovie);

        if (createUpdateMovieRequest.getNameRussian() != null) {
            stringJoiner.add("movie_name_russian = :name_russian, ");
        }
        if (createUpdateMovieRequest.getNameNative() != null) {
            stringJoiner.add("movie_name_native = :name_native, ");
        }
        if (createUpdateMovieRequest.getYearOfRelease() != 0) {
            stringJoiner.add("movie_year_of_release = :year_of_release, ");
        }
        if (createUpdateMovieRequest.getDescription() != null) {
            stringJoiner.add("movie_description = :description, ");
        }
        if (createUpdateMovieRequest.getRating() != 0) {
            stringJoiner.add("movie_rating = :rating, ");
        }
        if (createUpdateMovieRequest.getPrice() != 0) {
            stringJoiner.add("movie_price = :price, ");
        }
        if (createUpdateMovieRequest.getPicturePath() != null) {
            stringJoiner.add("movie_picture_path = :picture_path ");
        }
        stringJoiner.add("WHERE movie_id = :movie_id");
        return stringJoiner.toString();
    }

    MapSqlParameterSource getSqlParameterSourceForEditMovie(int movieId, CreateUpdateMovieRequest createUpdateMovieRequest) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if (movieId != 0) {
            parameterSource.addValue("movie_id", movieId);
        }
        if (createUpdateMovieRequest.getNameRussian() != null) {
            parameterSource.addValue("name_russian", createUpdateMovieRequest.getNameRussian());
        }
        if (createUpdateMovieRequest.getNameNative() != null) {
            parameterSource.addValue("name_native", createUpdateMovieRequest.getNameNative());
        }
        if (createUpdateMovieRequest.getYearOfRelease() != 0) {
            parameterSource.addValue("year_of_release", createUpdateMovieRequest.getYearOfRelease());
        }
        if (createUpdateMovieRequest.getDescription() != null) {
            parameterSource.addValue("description", createUpdateMovieRequest.getDescription());
        }
        if (createUpdateMovieRequest.getRating() != 0) {
            parameterSource.addValue("rating", createUpdateMovieRequest.getRating());
        }
        if (createUpdateMovieRequest.getPrice() != 0) {
            parameterSource.addValue("price", createUpdateMovieRequest.getPrice());
        }
        if (createUpdateMovieRequest.getPicturePath() != null) {
            parameterSource.addValue("picture_path", createUpdateMovieRequest.getPicturePath());
        }

        addMapSqlParametersForCountryIds(parameterSource, createUpdateMovieRequest.getCountriesIds());
        addMapSqlParametersForGenreIds(parameterSource, createUpdateMovieRequest.getGenresIds());
        return parameterSource;
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
        parametersMap.addValue("movie_id", movieId);
        namedParameterJdbcTemplate.update(removeMoviesCountries, parametersMap);
    }

    void removeMoviesGenresByMovieId(int movieId) {
        MapSqlParameterSource parametersMap = new MapSqlParameterSource();
        parametersMap.addValue("movie_id", movieId);
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
