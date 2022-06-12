package ru.yandex.practicum.javafilmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    void addReview(Review review);

    void updateReview(Review review);

    void deleteReview(Long id);

    Review getReviewById(Long id);

    List<Review> getReviewsOfFilm(Long filmId);

    void putLike(Long reviewId, Long userId);

    void putDislike(Long reviewId, Long userId);

    void deleteLike(Long reviewId, Long UserId);

    void deleteDislike(Long reviewId, Long userId);
}
