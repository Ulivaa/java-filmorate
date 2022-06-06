package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    int save(Film film);

    Collection<Film> returnAllFilms();

    void update(Film film);
}
