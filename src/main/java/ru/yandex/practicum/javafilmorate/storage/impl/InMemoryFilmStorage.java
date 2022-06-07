package ru.yandex.practicum.javafilmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new HashMap<>();

    @Override
    public int save(Film film) {
        films.put(film.getId(), film);
        return film.getId();
    }

    @Override
    public void delete(Integer film_id) {

    }

    @Override
    public Collection<Film> returnAllFilms() {
        return films.values();
    }

    @Override
    public void update(Film film) {
        films.put(film.getId(), film);
    }
}
