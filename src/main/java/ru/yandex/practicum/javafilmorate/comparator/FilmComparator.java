package ru.yandex.practicum.javafilmorate.comparator;

import ru.yandex.practicum.javafilmorate.model.Film;

import java.util.Comparator;

public class FilmComparator implements Comparator<Film> {
    @Override
    public int compare(Film o1, Film o2) {
        if (o1.getUsersLike() == null) {
            if (o2.getUsersLike() == null) {
                return o1.getName().compareTo(o2.getName());
            } else {
                return 1;
            }
        } else {
            if (o2.getUsersLike() == null) {
                return -1;
            } else {
                if (o1.getUsersLike().size() == o2.getUsersLike().size()) {
                    return o1.getName().compareTo(o2.getName());
                } else {
                    return o2.getUsersLike().size() - o1.getUsersLike().size();
                }
            }
        }
    }
}
