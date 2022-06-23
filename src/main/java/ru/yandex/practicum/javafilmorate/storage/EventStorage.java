package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.Event;

import java.time.LocalDateTime;
import java.util.Collection;

public interface EventStorage {

    void save(int entity_id , int user_id , LocalDateTime timestamp , String event_type , String operation);

    Collection<Event> findEventsUser(int id);
}
