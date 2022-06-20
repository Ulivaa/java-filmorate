package ru.yandex.practicum.javafilmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReadFilmStorage {
    // отдельно на чтение и запись для того чтобы можно было не добавлять ненужных функций для реализации в памяти.
    // Мне показалось это правильнее..
    Optional<Film> findFilmById(int filmId);

    Collection<Film> getPopular(int limit);

    List<Film> getCommonFilms(int userId, int friendId);

    List<Film> search(String query);
}
