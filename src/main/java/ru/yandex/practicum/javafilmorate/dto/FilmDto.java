package ru.yandex.practicum.javafilmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.javafilmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.javafilmorate.model.Film;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class FilmDto {

    private static int id = 0;
    private @NonNull String name;
    private String description;
    private LocalDate releaseDate;
    private Short duration;

    public Film mapToFilm(FilmDto filmDto) {

        if (filmDto.getName().isBlank()) {
            throw new IncorrectParameterException("name");
        }
        if (filmDto.getDescription().isBlank() || filmDto.getDescription().length() > 200) {
            throw new IncorrectParameterException("description");
        }
        if (filmDto.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new IncorrectParameterException("ReleaseDate");
        }
        if (filmDto.getDuration() < 0) {
            throw new IncorrectParameterException("Duration");
        }
        Film film = new Film(filmDto.getName());
        film.setReleaseDate(filmDto.getReleaseDate());
        film.setDescription(filmDto.getDescription());
        film.setDuration(filmDto.getDuration());
        film.setId(++id);
        return film;
    }
}