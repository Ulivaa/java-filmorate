package ru.yandex.practicum.javafilmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.Review;
import ru.yandex.practicum.javafilmorate.service.ReviewService;

import java.util.List;

@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/reviews")
    public Review addReview(@RequestBody Review review) {
        reviewService.addReview(review);
        return review;
    }

    @PutMapping("/reviews")
    public Review putReview(@RequestBody Review review) {
        reviewService.updateReview(review);
        return review;
    }

    @DeleteMapping("/reviews/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    @GetMapping("/reviews/{id}")
    public Review getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping("/reviews")
    public List<Review> getReviewsOfFilm(@RequestParam Long filmId, @RequestParam(defaultValue = "10") Integer count) {
        return reviewService.getReviewsOfFilm(filmId, count);
    }
}
