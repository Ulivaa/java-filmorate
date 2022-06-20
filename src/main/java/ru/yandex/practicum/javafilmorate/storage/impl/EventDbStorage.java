package ru.yandex.practicum.javafilmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.storage.EventStorage;

import java.time.LocalDateTime;

@Repository
public class EventDbStorage implements EventStorage {

    private final String saveEventQuery = "INSERT INTO Event" +
            " (entity_id , user_id , timestamp , event_type , operation) VALUES (? , ? , ? , ? , ?)";
    private final JdbcTemplate jdbcTemplate;

    public EventDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(int entity_id, int user_id, LocalDateTime timestamp, String event_type, String operation) {
        jdbcTemplate.update(saveEventQuery , entity_id , user_id , timestamp , event_type , operation);
    }
}
