package ru.yandex.practicum.javafilmorate.dto;


import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.javafilmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.javafilmorate.model.User;

import java.time.LocalDate;

@Slf4j
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
        if (userDto.getName() == null) {
            user.setName(userDto.getLogin());
        } else {
            user.setName(userDto.getName());
        }
        if (userDto.birthday.isAfter(LocalDate.now())) {
            log.error("Неверный формат данных");
            throw new IncorrectParameterException("birthday");
        } else {
            user.setBirthday(userDto.getBirthday());
        }
        if (user.getLogin().contains(" ") || !user.getEmail().contains("@")){
            log.error("Неверный формат данных");
            throw new IncorrectParameterException("email");
        }
        user.setId(++id);
        return user;
    }
}
