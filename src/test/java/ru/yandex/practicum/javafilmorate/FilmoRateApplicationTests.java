package ru.yandex.practicum.javafilmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.MPA;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.javafilmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = userStorage.findUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }


    @Test
    public void testUpdateUser() {
        User oldUser = new User(1, "updateLogin", "update@mail.ru", "name", LocalDate.of(2001, 11, 23));

        userStorage.update(oldUser);
        Optional<User> userOptional = userStorage.findUserById(1);


        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "update@mail.ru")
                );
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "updateLogin")
                );
    }

    @Test
    public void testSaveUser() {
        userStorage.save(new User(2, "login2", "email2@mail.ru", "name2", LocalDate.of(1991, 11, 23)));
        Optional<User> userOptional = userStorage.findUserById(2);


        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 2)
                );
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "email2@mail.ru")
                );
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "login2")
                );
    }

    @Test
    public void testFindFilmById() {

        Optional<Film> filmOptional = filmDbStorage.findFilmById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFilmUpdate() {
        filmDbStorage.update(new Film(1, "updateFilm", "updateDescr", LocalDate.of(1995, 04, 24), (short) 120, null, MPA.PG, null));
        Optional<Film> filmOptional = filmDbStorage.findFilmById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "updateFilm")
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "updateDescr")
                );
    }

    @Test
    public void testSaveFilm() {

        filmDbStorage.save(new Film(2, "film2", "descr2", LocalDate.of(2000, 04, 24), (short) 60, null, MPA.PG, null));
        Optional<Film> filmOptional = filmDbStorage.findFilmById(2);


        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 2)
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "film2")
                );
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "descr2")
                );
    }
}
