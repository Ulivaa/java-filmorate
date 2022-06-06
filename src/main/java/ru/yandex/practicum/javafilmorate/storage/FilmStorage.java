package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    void save(Film film);

    Collection<Film> returnAllFilms();

    void update(Film film);

}
