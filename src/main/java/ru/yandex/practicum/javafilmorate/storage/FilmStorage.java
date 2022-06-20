package ru.yandex.practicum.javafilmorate.storage;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    int save(Film film);

    void delete(Integer film_id);

    Collection<Film> returnAllFilms();

    Collection<Film> getPopularFilms(int count, int genreId, int year);

    void update(Film film);
}
