package ru.yandex.practicum.javafilmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Review;

import java.util.List;

@Component
public interface ReviewStorage {
    void addReview(Review review);

    void updateReview(Review review);

    void deleteReview(Long id);

    Review getReviewById(Long id);

    List<Review> getReviewsOfFilm(Long filmId);
}
