package ru.yandex.practicum.javafilmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.dto.FilmDto;
import ru.yandex.practicum.javafilmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film addFilm(FilmDto filmDto) {
        Film film = filmDto.mapToFilm(filmDto);
        if (validateDate(film)) {
            log.error("Неверный формат данных");
            throw new RuntimeException();
        }
        filmStorage.save(film);
        log.info("Добавлен объект {}", film.getName());
        return findFilmById(film.getId());
    }

    public Film updateFilm(Film film) {
        if (film.getId() == 0 || validateDate(film)) {
            log.error("Неверный формат данных");
            throw new RuntimeException();
        }
        filmStorage.save(film);
        log.info("Обновлен объект {}", film.getName());
        return findFilmById(film.getId());
    }

    public Collection<Film> returnAllFilms() {
        return filmStorage.returnAllFilms();
    }

    // не знаю насколько правильно в бизнес логике менять сами данные. и не совсем понимаю как это сделать условно из класса хранилища непосредственно
    public void addUserLike(Integer id, Integer userId) {
        Film film = findFilmById(id);
        User user = userService.findUserById(userId);
        film.setUserLike(user);
        filmStorage.save(film);
        log.info("Добавлен лайк фильму {}, от пользователя {}", film.getName(), user.getName());
    }

    public void deleteUserLike(Integer id, Integer userId) {
        Film film = findFilmById(id);
        User user = userService.findUserById(userId);
        film.removeUserLike(user);
        filmStorage.save(film);
        log.info("Удален лайк фильму {}, от пользователя {}", film.getName(), user.getName());
    }

    public Collection<Film> FirstFilmsWithCountLike(Integer count) {
        // я пыталась сначала вернуть o2.getUsersLike().size() - o1.getUsersLike().size(),
        // но у меня падала ошибка, когда у фильма нет лайков.
        // Хотя мне казалось, если я создаю сет через нью сразу, то размер будет равен 0.
        // Можно ли как-то избежать такого колоза ниже и в одну строку сравнить?
        return filmStorage.returnAllFilms().stream().sorted((o1, o2) -> {
            int size1;
            int size2;
            if (o1.getUsersLike() == null) {
                size1 = 0;
            } else {
                size1 = o1.getUsersLike().size();
            }
            if (o2.getUsersLike() == null) {
                size2 = 0;
            } else {
                size2 = o2.getUsersLike().size();
            }
            return size2 - size1;
        }).limit(count).collect(Collectors.toList());
    }

    private boolean validateDate(Film film) {
        return (film.getDescription() != null && film.getDescription().length() > 200)
                || film.getDuration() < 0 || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28));
    }

    public Film findFilmById(Integer id) {
        return filmStorage.returnAllFilms()
                .stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new FilmNotFoundException(String.format("Фильм № %d не найден", id)));
    }
}
