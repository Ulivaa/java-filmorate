package ru.yandex.practicum.javafilmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.comparator.FilmComparator;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.GENRE;
import ru.yandex.practicum.javafilmorate.service.FilmService;
import ru.yandex.practicum.javafilmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new HashMap<>();
    private FilmStorage filmStorage;
    private FilmService filmService;

    @Override
    public int save(Film film) {
        films.put(film.getId(), film);
        return film.getId();
    }

    @Override
    public void delete(Integer film_id) {

    }

    @Override
    public Collection<Film> returnAllFilms() {
        return films.values();
    }

    @Override
    public Collection<Film> getPopularFilms(int count, int genreId, int year) {
        Collection<Film> films = new ArrayList<>();
        if (genreId == 0 && year == 0) {
            return filmService.firstFilmsWithCountLike(count);
        } else if (genreId == 0) {
            for (Film film: filmStorage.returnAllFilms()) {
                if (film.getReleaseDate().getYear() == year) {
                    films.add(film);
                }
            }
        } else if(year == 0) {
            for (Film film: filmStorage.returnAllFilms()) {
                ArrayList<Integer> genres = new ArrayList<>();
                if (film.getGenres()!=null){
                    for (GENRE genre: film.getGenres()) {
                        genres.add(genre.getId());
                    } if (genres.contains(genreId)) {
                        films.add(film);
                    }
                    genres.clear();
                }
            }
        } else {
            for (Film film: filmStorage.returnAllFilms()) {
                ArrayList<Integer> genres = new ArrayList<>();
                if (film.getGenres()!=null){
                    for (GENRE genre: film.getGenres()) {
                        genres.add(genre.getId());
                    }
                    if (genres.contains(genreId) && film.getReleaseDate().getYear() == year) {
                        films.add(film);
                    }
                    genres.clear();
                }
            }
        }
        return films.stream().sorted(new FilmComparator()).limit(count).collect(Collectors.toList());
    }

    @Override
    public void update(Film film) {
        films.put(film.getId(), film);
    }
}
