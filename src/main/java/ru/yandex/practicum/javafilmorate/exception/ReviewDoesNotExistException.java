package ru.yandex.practicum.javafilmorate.exception;

public class ReviewDoesNotExistException extends RuntimeException {
    public ReviewDoesNotExistException() {
        super("Ревью c таким id  не существует");
    }
}
