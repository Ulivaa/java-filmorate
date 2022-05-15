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

    public Collection<Film> firstFilmsWithCountLike(Integer count) {
//        Попробовала снова нет, явно падает на моменте, когда хочет взять size() у объекта, которого не существует(null).
//        Не проходит 2 теста Film get Popular count и Film get Popular count 2. Вернула изначальный вариант.
//        Не понимаю, почему при создании usersLike через new HashSet() он null.
//        Насколько я понимаю ему должно выделиться место в памяти и при usersLike.size() он должен вернуть 0(как мне кажется)
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

//        return filmStorage.returnAllFilms().stream().
//                sorted((o1, o2) -> o2.getUsersLike().size() - o1.getUsersLike().size()).
//                limit(count).collect(Collectors.toSet());
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
