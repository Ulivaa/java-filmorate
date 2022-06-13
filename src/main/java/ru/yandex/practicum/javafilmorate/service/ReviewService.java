package ru.yandex.practicum.javafilmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.javafilmorate.exception.ReviewDoesNotExistException;
import ru.yandex.practicum.javafilmorate.exception.UserNotFoundException;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.storage.ReviewStorage;
import ru.yandex.practicum.javafilmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.javafilmorate.storage.impl.UserDbStorage;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewStorage storage;
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage,
                         FilmDbStorage filmStorage,
                         UserDbStorage userStorage) {
        this.storage = reviewStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addReview(Review review) {
        storage.addReview(review);
    }

    public void updateReview(Review review) {
        storage.getReviewById(review.getReviewId()).orElseThrow(() -> new ReviewDoesNotExistException());

        storage.updateReview(review);
    }

    public void deleteReview(Long id) {
        storage.deleteReview(id);
    }

    public Review getReviewById(Long id) {
        return storage.getReviewById(id).orElseThrow(() -> new ReviewDoesNotExistException());
    }

    public List<Review> getReviewsOfFilm(Long filmId, int count) {

        filmStorage.findFilmById(filmId.intValue()).orElseThrow(() ->
                new FilmNotFoundException("Фильм с id " + filmId + " не найден"));

        return storage.getReviewsOfFilm(filmId).subList(0, count);
    }

    public void putLike(Long reviewId, Long userId) {
        userStorage.findUserById(userId.intValue()).orElseThrow(() -> new UserNotFoundException("user не найден"));

        deleteReaction(reviewId, userId);

        storage.putLike(reviewId, userId);
    }

    public void putDislike(Long reviewId, Long userId) {
        userStorage.findUserById(userId.intValue()).orElseThrow(() -> new UserNotFoundException("user не найден"));

        storage.putDislike(reviewId, userId);
    }

    public void deleteReaction(Long reviewId, Long userId) {
        userStorage.findUserById(userId.intValue()).orElseThrow(() -> new UserNotFoundException("user не найден"));

        storage.deleteDislike(reviewId, userId);
        storage.deleteLike(reviewId, userId);
    }
}
