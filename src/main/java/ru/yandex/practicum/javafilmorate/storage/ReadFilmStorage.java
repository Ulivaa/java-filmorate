package ru.yandex.practicum.javafilmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.Collection;

@Repository
public interface ReadFilmStorage {
    Film findFilmById(int film_id);

    Collection<Film> getPopular(int limit);

}
