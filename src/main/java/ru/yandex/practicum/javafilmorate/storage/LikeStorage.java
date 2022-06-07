package ru.yandex.practicum.javafilmorate.storage;

import java.util.Map;
import java.util.Set;

public interface LikeStorage {
    void save(int film_id, int user_id);

    void delete(int film_id, int user_id);


    Set<Integer> allForUser(int userId);

    Map<Integer, Set<Integer>> allExceptUser(int userId);

}
