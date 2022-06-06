package ru.yandex.practicum.javafilmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.storage.FilmStorage;
import ru.yandex.practicum.javafilmorate.storage.LikeStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserService userService;
    private LikeStorage likeStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, UserService userService, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.likeStorage = likeStorage;
    }

    public Film addFilm(Film film) {
        if (validateDate(film)) {
            log.error("Неверный формат данных");
            throw new RuntimeException();
        }
        filmStorage.save(film);
        log.info("Добавлен объект {}", film.getName());
        return findFilmById(film.getId());
    }

    public Film updateFilm(Film film) {
        Film filmUpdate = findFilmById(film.getId());
        if (film.getId() == 0 || validateDate(film)) {
            log.error("Неверный формат данных");
            throw new RuntimeException();
        }
        if (filmUpdate == null) {
            log.error("Такого фильма пока не существует");
            throw new RuntimeException();
        }
        if (film.getName() != null) {
            filmUpdate.setName(film.getName());
        }
        if (film.getDescription() != null) {
            filmUpdate.setDescription(film.getDescription());
        }
        if (film.getDuration() != null) {
            filmUpdate.setDuration(film.getDuration());
        }
        if (film.getUsersLike() != null) {
            filmUpdate.setUsersLike(film.getUsersLike());
        }
        if (film.getReleaseDate() != null) {
            filmUpdate.setReleaseDate(film.getReleaseDate());
        }
        if (film.getMpa() != null) {
            filmUpdate.setMpa(film.getMpa());
        }
        filmStorage.update(filmUpdate);
        log.info("Обновлен объект {}", film.getName());
        return findFilmById(film.getId());
    }

    public Collection<Film> returnAllFilms() {
        return filmStorage.returnAllFilms();
    }

    public void addUserLike(Integer film_id, Integer userId) {
        if (findFilmById(film_id) != null && userService.findUserById(userId) != null) {
            likeStorage.save(film_id, userId);
        }
    }

    public void deleteUserLike(Integer id, Integer userId) {
        if (findFilmById(id) != null && userService.findUserById(userId) != null) {
            likeStorage.delete(id, userId);
        }
    }

    public Collection<Film> firstFilmsWithCountLike(Integer count) {
        return filmStorage.getPopular(count);
    }

    private boolean validateDate(Film film) {
        return (film.getDescription() != null && film.getDescription().length() > 200)
                || film.getDuration() < 0 || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)) || film.getId() < 0;
    }

    public Film findFilmById(int id) {
        Film film = filmStorage.findFilmById(id);
        if (film != null) {
            return film;
        } else {
            throw new FilmNotFoundException(String.format("Фильм № %d не найден", id));
        }
    }
}
