package ru.yandex.practicum.javafilmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class User {
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private int id;
    private @NonNull String login;
    private @NonNull String email;
    private String name;
    private LocalDate birthday;
    @JsonIgnore
    private HashSet<User> userFriends = new HashSet<>();

    public void setUserFriend(User user) {
        userFriends.add(user);
    }

    public void removeUserFriend(User user) {
        userFriends.remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}

