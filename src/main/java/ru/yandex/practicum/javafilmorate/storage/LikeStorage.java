package ru.yandex.practicum.javafilmorate.storage;

public interface LikeStorage {
    void save(int film_id, int user_id);

    void delete(int film_id, int user_id);

}
