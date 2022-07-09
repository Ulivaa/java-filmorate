package ru.yandex.practicum.javafilmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.service.FilmService;

import javax.validation.constraints.Positive;
import java.util.Collection;

@Slf4j
@RestController
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @DeleteMapping("/films/{filmId}")
    public void deleteFilm(@PathVariable Integer filmId) {
        filmService.deleteFilm(filmId);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/films")
    public Collection<Film> returnAllFilms() {
        return filmService.returnAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film returnFilmById(@PathVariable Integer id) {
        return filmService.findFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addUserLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addUserLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteUserLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteUserLike(id, userId);
    }

    @GetMapping("/films/common")
    public Collection<Film> commonFilm(@RequestParam Integer userId, @RequestParam Integer friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/films/popular")
    public Collection<Film> returnPopularFilms(@RequestParam(required = false, defaultValue = "10")
                                            @Positive(message = "Count must be positive") int count,
                                     @RequestParam(required = false, defaultValue = "0") int genreId,
                                     @RequestParam(required = false, defaultValue = "0") int year) {
        log.info("Get {} popular films", count);
        return filmService.returnPopularFilm(count, genreId, year);
    }

    @GetMapping("/films/search")
    public Collection<Film> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "title") String by
    ) {
        return filmService.search(query, by);
    }
}

// example
// {
//  "name": "4",
//  "description": "adipisicing",
//  "releaseDate": "1967-03-25",
//  "duration": 100
//}
