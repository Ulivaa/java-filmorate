package ru.yandex.practicum.javafilmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.dto.FilmDto;
import ru.yandex.practicum.javafilmorate.model.Film;


import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();

    @PostMapping("/films")
    public void addFilm(@RequestBody FilmDto filmDto) {
        Film film = filmDto.mapToFilm(filmDto);
        if ((film.getDescription() != null && film.getDescription().length() > 200)
                || film.getDuration().isNegative()
                || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Неверный формат данных");
            throw new RuntimeException();
        }
        films.put(film.getId(), film);
        log.info("Добавлен объект {}", film.getName());
    }

    @PutMapping("/films")
    public void updateFilm(@RequestBody Film film) {
        if (film.getId() == 0 || (film.getDescription() != null && film.getDescription().length() > 200)
                || film.getDuration().isNegative() || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Неверный формат данных");
            throw new RuntimeException();
        }
        films.put(film.getId(), film);
        log.info("Обновлен объект {}", film.getName());
    }

    @GetMapping("/films")
    public Collection<Film> returnAllFilms() {
        return films.values();
    }

}
