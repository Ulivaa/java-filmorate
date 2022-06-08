package ru.yandex.practicum.javafilmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.javafilmorate.model.GENRE;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GenreService {
    public GENRE findGenreById(Integer id) {
        for (GENRE obj : GENRE.values()) {
            if (Double.compare(obj.getId(), id) == 0) {
                return obj;
            }
        }
        throw new GenreNotFoundException(String.format("Genre c id № %d не найден", id));
    }

    public Collection<GENRE> returnAllGenres() {
        Collection<GENRE> genres = new HashSet<>();
        for (GENRE obj : GENRE.values()) {
            genres.add(obj);


        }
        return genres.stream().sorted((o, o2) -> o.getId() - o2.getId()).collect(Collectors.toList());

    }

}
