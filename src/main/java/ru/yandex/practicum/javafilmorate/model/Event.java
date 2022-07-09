package ru.yandex.practicum.javafilmorate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Event {

    private int eventId;
    private int entityId;
    private int userId;
    private LocalDateTime timestamp;
    private String eventType;
    private String operation;

    public Event(int eventId, int entityId, int userId, LocalDateTime timestamp, String eventType, String operation) {
        this.eventId = eventId;
        this.entityId = entityId;
        this.userId = userId;
        this.timestamp = timestamp;
        this.eventType = eventType;
        this.operation = operation;
    }
}
