package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.dao.ReviewDao;
import com.hlushkov.movieland.entity.Review;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class JdbcReviewDao implements ReviewDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final String addReview;

    @Override
    public void save(Review review) {
        MapSqlParameterSource parametersMap = getSqlParameterSource(review.getUser().getId(), review.getMovieId(),
                review.getText());
        namedParameterJdbcTemplate.update(addReview, parametersMap);
    }

    MapSqlParameterSource getSqlParameterSource(int userId, int movieId, String text) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("user_id", userId);
        parameterSource.addValue("movie_id", movieId);
        parameterSource.addValue("text", text);
        return parameterSource;
    }

}
