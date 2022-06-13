package ru.yandex.practicum.javafilmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.storage.ReviewStorage;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewStorage storage;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage) {
        this.storage = reviewStorage;
    }

    public void addReview(Review review) {
        storage.addReview(review);
    }

    public void updateReview(Review review) {
        storage.updateReview(review);
    }

    public void deleteReview(Long id) {
        storage.deleteReview(id);
    }

    public Review getReviewById(Long id) {
        return storage.getReviewById(id);
    }

    public List<Review> getReviewsOfFilm(Long filmId, int count) {
        return storage.getReviewsOfFilm(filmId).subList(0, count);
    }

    public void putLike(Long reviewId, Long userId) {
        deleteReaction(reviewId, userId);

        storage.putLike(reviewId, userId);
    }

    public void putDislike(Long reviewId, Long userId) {
        storage.putDislike(reviewId, userId);
    }

    public void deleteReaction(Long reviewId, Long userId) {
        storage.deleteDislike(reviewId, userId);
        storage.deleteLike(reviewId, userId);
    }
}
