package ru.yandex.practicum.javafilmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.javafilmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class FilmDto {

    private static int id = 0;
    private @NonNull String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    public Film mapToFilm(FilmDto filmDto) {

        Film film = new Film(filmDto.getName());
        film.setId(++id);
        film.setReleaseDate(filmDto.getReleaseDate());
        film.setDescription(filmDto.getDescription());
        film.setDuration(Duration.ofMinutes(filmDto.getDuration()));
        return film;
    }


}