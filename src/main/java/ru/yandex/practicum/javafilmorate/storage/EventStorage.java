package ru.yandex.practicum.javafilmorate.storage;

import java.time.LocalDateTime;

public interface EventStorage {

    void save(int entity_id , int user_id , LocalDateTime timestamp , String event_type , String operation);
}
