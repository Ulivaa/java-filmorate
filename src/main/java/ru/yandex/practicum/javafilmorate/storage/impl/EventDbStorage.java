package ru.yandex.practicum.javafilmorate.storage.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.model.Event;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.EventStorage;
import ru.yandex.practicum.javafilmorate.storage.FriendshipStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class EventDbStorage implements EventStorage {

    private final String saveEventQuery = "INSERT INTO Event" +
            " (entity_id , user_id , timestamp , event_type , operation) VALUES (? , ? , ? , ? , ?)";
    private final String findEventsUser = "SELECT * FROM event WHERE user_id = ?";
    private final JdbcTemplate jdbcTemplate;
    private FriendshipStorage friendshipStorage;

    public EventDbStorage(JdbcTemplate jdbcTemplate, @Lazy FriendshipStorage friendshipStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendshipStorage = friendshipStorage;
    }

    @Override
    public void save(int entityId, int userId, LocalDateTime timestamp, String eventType, String operation) {
        jdbcTemplate.update(saveEventQuery , entityId , userId , timestamp , eventType , operation);
    }

    public Collection<Event> findEventsUser(int id){
        List<Event> eventsFriends = new ArrayList<>();
        Collection<User> friends = friendshipStorage.getFriends(id);
        for(User friend : friends){
            eventsFriends.addAll(jdbcTemplate.query(findEventsUser, this::makeEvent, friend.getId()));
        }
        return eventsFriends;
    }

    private Event makeEvent(ResultSet rs, int i) throws SQLException {
        String[] split = (rs.getString("timestamp")).split("\\.");
        LocalDateTime timestamp = LocalDateTime.parse(split[0] , DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return new Event(rs.getInt("event_id") ,
                rs.getInt("entity_id") ,
                rs.getInt("user_id") ,
                timestamp ,
                rs.getString("event_type") ,
                rs.getString("operation"));
    }
}
