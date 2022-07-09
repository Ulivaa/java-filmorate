package ru.yandex.practicum.javafilmorate.storage.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.exception.ReviewDoesNotExistException;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.storage.EventStorage;
import ru.yandex.practicum.javafilmorate.storage.ReviewStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    private final EventStorage eventStorage;


    public ReviewDbStorage(JdbcTemplate jdbcTemplate, EventStorage eventStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventStorage = eventStorage;
    }

    @Override
    public void addReview(Review review) {
        String sqlQuery = "INSERT INTO Reviews VALUES(?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                review.getReviewId(),
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getUseful());
        eventStorage.save(Math.toIntExact(review.getReviewId()) , Math.toIntExact(review.getUserId()) ,
                LocalDateTime.now() , "REVIEW" , "ADD");
    }

    @Override
    public void updateReview(Review review) throws
            DataAccessException {
        String sqlQuery = "UPDATE Reviews SET " +
                "content = ?," +
                "is_positive = ?," +
                "user_id = ?," +
                "film_id = ?," +
                "useful = ? " +
                "WHERE review_id = ?";

        jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getUseful(),
                review.getReviewId());
    }

    @Override
    public List<Review> getReviews() {
        String sqlQuery = "SELECT * FROM Reviews";

        return jdbcTemplate.query(sqlQuery, this::reviewFromSQL);
    }

    @Override
    public void deleteReview(Long id) {
        Review review = getReviewById(id).orElseThrow();
        String sqlQuery = "DELETE FROM Reviews_reaction WHERE review_id = ?";
        jdbcTemplate.update(sqlQuery, id);

        sqlQuery = "DELETE FROM Reviews WHERE review_id = ?";
        jdbcTemplate.update(sqlQuery, id);
        eventStorage.save(Math.toIntExact(id) , Math.toIntExact(review.getReviewId()) ,
                LocalDateTime.now() , "REVIEW" , "REMOVE");
    }

    @Override
    public Optional<Review> getReviewById(Long id) {
        String sqlQuery = "SELECT * FROM Reviews WHERE review_id = ?";
        try {


            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::reviewFromSQL, id));
        } catch (DataAccessException e) {
            throw new ReviewDoesNotExistException();
        }
    }

    @Override
    public List<Review> getReviewsOfFilm(Long filmId) {
        String sqlQuery = "SELECT * FROM Reviews WHERE film_id = ?";

        return jdbcTemplate.query(sqlQuery, this::reviewFromSQL, filmId);
    }

    @Override
    public void putLike(Long reviewId, Long userId) {
        String sqlQuery = "INSERT INTO Reviews_reaction VALUES (?, ?, true)";

        jdbcTemplate.update(sqlQuery, reviewId, userId);
    }

    @Override
    public void putDislike(Long reviewId, Long userId) {
        String sqlQuery = "INSERT INTO Reviews_reaction VALUES (?, ?, false)";

        jdbcTemplate.update(sqlQuery, reviewId, userId);
    }

    @Override
    public void deleteLike(Long reviewId, Long userId) {
        String sqlQuery = "DELETE FROM Reviews_reaction WHERE review_id = ? AND user_id = ? AND reaction = true";

        jdbcTemplate.update(sqlQuery, reviewId, userId);
    }

    @Override
    public void deleteDislike(Long reviewId, Long userId) {
        String sqlQuery = "DELETE FROM Reviews_reaction WHERE review_id = ? AND user_id = ? AND reaction = false";

        jdbcTemplate.update(sqlQuery, reviewId, userId);
    }

    private Review reviewFromSQL(ResultSet rs, Integer rowNum) throws SQLException {


        Long reviewId = rs.getLong("review_id");
        String content = rs.getString("content");
        Boolean isPositive = rs.getBoolean("is_positive");
        Long userId = rs.getLong("user_id");
        Long filmId = rs.getLong("film_id");
        Integer useful = rs.getInt("useful");

        return new Review(reviewId, content, isPositive, userId, filmId, useful);
    }
}
