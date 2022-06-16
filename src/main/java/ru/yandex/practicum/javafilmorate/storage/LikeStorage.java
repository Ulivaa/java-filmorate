package ru.yandex.practicum.javafilmorate.storage;

import java.util.Map;
import java.util.Set;

public interface LikeStorage {
    void save(int filmId, int userId);

    void delete(int filmId, int userId);

    Set<Integer> allForUser(int userId);

    Map<Integer, Set<Integer>> allExceptUser(int userId);

}
