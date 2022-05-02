package ru.yandex.practicum.javafilmorate.dto;


import lombok.*;
import ru.yandex.practicum.javafilmorate.model.User;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private static int id = 0;
    private @NonNull String login;
    private @NonNull String email;
    private String name;
    private LocalDate birthday;

    public User mapToUser(UserDto userDto) {
        User user = new User(userDto.getLogin(), userDto.getEmail());
        user.setId(++id);
        if (userDto.getName() == null) {
            user.setName(userDto.getLogin());
        } else {
            user.setName(userDto.getName());
        }
        if (userDto.birthday.isAfter(LocalDate.now())) {
            throw new RuntimeException("День рождения не может быть в будущем");
        } else {
            user.setBirthday(userDto.getBirthday());
        }
        return user;
    }
}
