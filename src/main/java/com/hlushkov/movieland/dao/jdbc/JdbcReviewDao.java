package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.dao.ReviewDao;
import com.hlushkov.movieland.dao.jdbc.mapper.ReviewRowMapper;
import com.hlushkov.movieland.entity.Review;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class JdbcReviewDao implements ReviewDao {
    private final ReviewRowMapper reviewRowMapper = new ReviewRowMapper();
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final String addReview;
    private final String findReviewsByMovieId;
    //private final String removeMoviesReviewByMovieId;

    @Override
    public void addReview(Review review) {
        MapSqlParameterSource parametersMap = getSqlParameterSource(review.getUserId(), review.getMovieId(),
                review.getText());
        namedParameterJdbcTemplate.update(addReview, parametersMap);
    }

    @Override
    public List<Review> findReviewsByMovieId(int movieId) {
        return jdbcTemplate.query(findReviewsByMovieId, reviewRowMapper, movieId);
    }

/*    @Override
    public void removeReviewsByMovieId(int movieId) {
        return;jdbcTemplate.update(, movieId);
    }*/

    MapSqlParameterSource getSqlParameterSource(int userId, int movieId, String text) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("user_id", userId);
        parameterSource.addValue("movie_id", movieId);
        parameterSource.addValue("review_text", text);
        return parameterSource;
    }

}
