package ru.yandex.practicum.javafilmorate.exception;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(String message){
        super(message);
    }
}
