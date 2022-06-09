package ru.yandex.practicum.javafilmorate.model;

import lombok.Data;

@Data
public class Review {
    private Long reviewId;
    private String content;
    private Boolean isPositive;
    private Long userId;
    private Long filmId;
    private Integer useful;

    public Review(Long reviewId, String content, Boolean isPositive, Long userId, Long filmId, Integer useful) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
        this.useful = useful;
    }
}
