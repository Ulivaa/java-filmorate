package ru.yandex.practicum.javafilmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Film {
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private int id;
    private @NonNull String name;
    private String description;
    private LocalDate releaseDate;
    private Short duration;

    private HashSet<User> usersLike = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.getId();
    }

    public void setUserLike(User user) {
        usersLike.add(user);
    }

    public void removeUserLike(User user) {
        usersLike.remove(user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
