package ru.yandex.practicum.javafilmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.javafilmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.javafilmorate.exception.ReviewDoesNotExistException;
import ru.yandex.practicum.javafilmorate.exception.UserNotFoundException;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.storage.ReviewStorage;
import ru.yandex.practicum.javafilmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.javafilmorate.storage.impl.UserDbStorage;

import java.util.Comparator;
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
        addId(review);
        validate(review);
        storage.addReview(review);
    }

    public void updateReview(Review review) {
        storage.getReviewById(review.getReviewId()).orElseThrow(ReviewDoesNotExistException::new);

        validate(review);
        storage.updateReview(review);

    }

    public List<Review> getReviews() {
        return storage.getReviews();
    }

    public void deleteReview(Long id) {

        storage.deleteReview(id);
    }

    public Review getReviewById(Long id) {
        return storage.getReviewById(id).orElseThrow(ReviewDoesNotExistException::new);
    }

    public List<Review> getReviewsOfFilm(Long filmId, int count) {
        List<Review> reviews = null;

        if (filmId != 0) {
            filmStorage.findFilmById(filmId.intValue()).orElseThrow(() ->
                    new FilmNotFoundException("Фильм с id " + filmId + " не найден"));

            reviews = storage.getReviewsOfFilm(filmId);
        } else {
            reviews = storage.getReviews();

        }


        if (reviews.size() > count) {
            reviews = storage.getReviewsOfFilm(filmId).subList(0, count);
        }

        reviews.sort(Comparator.comparingInt(Review::getUseful).reversed());

        return reviews;
    }

    public void putLike(Long reviewId, Long userId) {
        userStorage.findUserById(userId.intValue()).orElseThrow(() -> new UserNotFoundException("user не найден"));

        //увеличение рейтинга
        Review review = storage.getReviewById(reviewId).get();
        review.setUseful(review.getUseful() + 1);
        updateReview(review);


        storage.putLike(reviewId, userId);
    }

    public void putDislike(Long reviewId, Long userId) {
        userStorage.findUserById(userId.intValue()).orElseThrow(() -> new UserNotFoundException("user не найден"));

        //уменьшение рейтинга
        Review review = storage.getReviewById(reviewId).get();
        Integer useful = review.getUseful();

        review.setUseful(useful - 1);

        updateReview(review);

        storage.putDislike(reviewId, userId);
    }


    public void deleteDislike(Long reviewId, Long userId) {
        userStorage.findUserById(userId.intValue()).orElseThrow(() -> new UserNotFoundException("user не найден"));

        //возвращение рейтинга
        Review review = storage.getReviewById(reviewId).get();
        review.setUseful(review.getUseful() + 1);
        updateReview(review);

        storage.deleteDislike(reviewId, userId);
    }


    public void deleteLike(Long reviewId, Long userId) {
        userStorage.findUserById(userId.intValue()).orElseThrow(() -> new UserNotFoundException("user не найден"));

        //возвращение рейтинга
        Review review = storage.getReviewById(reviewId).get();
        review.setUseful(review.getUseful() - 1);
        updateReview(review);

        storage.deleteLike(reviewId, userId);
    }

    private void addId(Review review) {
        if (review.getReviewId() == null) {
            List<Review> reviews = storage.getReviews();
            Long newId = 1L;
            reviews.sort(Comparator.comparingInt(review2 -> review2.getReviewId().intValue()));
            System.out.println(reviews);

            for (Review r : reviews) {
                if (r.getReviewId() == newId) {
                    newId++;
                } else {
                    break;
                }
            }

            review.setReviewId(newId);
        }
    }

    public void validate(Review review) {
        filmStorage.findFilmById(review.getFilmId().intValue())
                .orElseThrow(() -> new FilmNotFoundException("Фильма не суеществует с id " + review.getFilmId()));

        userStorage.findUserById(review.getUserId().intValue())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден с id " + review.getUserId()));

        if (review.getIsPositive() == null) {
            throw new IncorrectParameterException("isPositive");
        }

        if (review.getFilmId() == null) {
            throw new IncorrectParameterException("filmId");
        }

        if (review.getUserId() == null) {
            throw new IncorrectParameterException("userId");
        }
    }
}
