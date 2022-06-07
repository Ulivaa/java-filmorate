package ru.yandex.practicum.javafilmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.SearchType;
import ru.yandex.practicum.javafilmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
public class FilmController {
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        return filmService.addFilm(film);
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

    @GetMapping("/films/popular")
    public Collection<Film> returnFilmsWithCountLike(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.firstFilmsWithCountLike(count);
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
