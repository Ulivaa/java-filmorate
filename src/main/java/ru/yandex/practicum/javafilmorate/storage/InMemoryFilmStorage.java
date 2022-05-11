package ru.yandex.practicum.javafilmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new HashMap<>();

    @Override
    public void save(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public Collection<Film> returnAllFilms() {
        return films.values();
    }
}
