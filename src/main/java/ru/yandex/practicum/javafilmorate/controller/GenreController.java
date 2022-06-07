package ru.yandex.practicum.javafilmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.javafilmorate.model.GENRE;
import ru.yandex.practicum.javafilmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@RestController
public class GenreController {
    GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genres/{id}")
    public GENRE findGenresById(@PathVariable Integer id) {
        return genreService.findGenreById(id);
    }

    @GetMapping("/genres")
    public Collection<GENRE> returnAllGenres() {
        return genreService.returnAllGenres();
    }
}

