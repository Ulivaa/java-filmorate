package ru.yandex.practicum.javafilmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.javafilmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.storage.FilmStorage;
import ru.yandex.practicum.javafilmorate.storage.LikeStorage;
import ru.yandex.practicum.javafilmorate.storage.ReadFilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private FilmStorage filmStorage;
    private ReadFilmStorage readFilmStorage;
    private UserService userService;
    private LikeStorage likeStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, UserService userService, LikeStorage likeStorage, ReadFilmStorage readFilmStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.likeStorage = likeStorage;
        this.readFilmStorage = readFilmStorage;
    }

    public Film addFilm(Film film) {
        if (!isValidateDate(film)) {
            log.error("Неверный формат данных");
            throw new RuntimeException();
        }
        int id = filmStorage.save(film);
        log.info("Добавлен объект {}", film.getName());
        return findFilmById(id);
    }

    public void deleteFilm(Integer film_id) {
        if (findFilmById(film_id) != null) {
            filmStorage.delete(film_id);
        }
    }

    public Film updateFilm(Film film) {
        Film filmUpdate = findFilmById(film.getId());
        if (film.getId() == 0 || !isValidateDate(film)) {
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
        if (film.getGenres() != null) {
            filmUpdate.setGenres(film.getGenres());
        }

        filmStorage.update(filmUpdate);

        // нужно для прохождения тестов
        Film updateFilm = findFilmById(film.getId());
        if (film.getGenres()!= null && film.getGenres().isEmpty()){
            if (updateFilm.getGenres() == null){
                updateFilm.setGenres(new HashSet<>());
            }
        }
        log.info("Обновлен объект {}", film.getName());
        return updateFilm;
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

    public List<Film> getCommonFilms(Integer user_id, Integer friend_id){
        return readFilmStorage.getCommonFilms(user_id, friend_id);

    }

    public Collection<Film> firstFilmsWithCountLike(Integer count) {
        return readFilmStorage.getPopular(count);
    }

    private boolean isValidateDate(Film film) {
        if (film.getName().isBlank()) {
            throw new IncorrectParameterException("name");
        }
        if (film.getDescription().isBlank() || film.getDescription().length() > 200) {
            throw new IncorrectParameterException("description");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new IncorrectParameterException("ReleaseDate");
        }
        if (film.getDuration() < 0) {
            throw new IncorrectParameterException("Duration");
        }
        if (film.getMpa() == null) {
            throw new IncorrectParameterException("MPA");
        }
        return true;
    }

    public Film findFilmById(int id) {
        return readFilmStorage.findFilmById(id).orElseThrow(() -> new FilmNotFoundException(String.format("Фильм № %d не найден", id)));
    }
}
