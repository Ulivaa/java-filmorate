package ru.yandex.practicum.javafilmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Collection<User> usersLike = new HashSet<>();
    private MPA mpa;
    //    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Collection<GENRE> genres;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("mpa", mpa.toString());
        return values;
    }
}
