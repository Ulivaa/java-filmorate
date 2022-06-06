package ru.yandex.practicum.javafilmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ReadFilmStorage {
    Optional<Film> findFilmById(int film_id);

    Collection<Film> getPopular(int limit);
}
