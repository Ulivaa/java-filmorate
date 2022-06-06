package ru.yandex.practicum.javafilmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.ReadUserStorage;
import ru.yandex.practicum.javafilmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Repository("UserDbStorage")
public class UserDbStorage implements UserStorage, ReadUserStorage {

    private final String saveUserQuery = "insert into users(login, name, email, birthday) values (?, ?, ?, ?)";
    private final String updateUserQuery = "update users set login = ?, name = ?, email = ?, birthday = ? where user_id =? ";
    private final String findUserByIdQuery = "select * from users where user_id = ?";
    private final String findUserByEmailQuery = "select * from users where email = ?";
    private final String findUsersLikeQuery = "SELECT * from users u JOIN likes l on u.user_id = l.user_id WHERE film_id = ?";

    JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        return simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(updateUserQuery,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                Date.valueOf(user.getBirthday()),
                user.getId());
    }

    @Override
    public Optional<User> findUserById(int id) {
        return jdbcTemplate.query(
                findUserByIdQuery, (rs, rowNum) -> makeUser(rs, rowNum), id
        ).stream().findAny();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return jdbcTemplate.query(
                findUserByEmailQuery, (rs, rowNum) -> makeUser(rs, rowNum), email
        ).stream().findAny();
    }

    @Override
    public Collection<User> returnAllUsers() {
        return jdbcTemplate.query("SELECT * from users", (rs, rowNum) -> makeUser(rs, rowNum));
    }

    public Collection<User> findUsersLikeToFilm(Integer film_id) {
        return jdbcTemplate.query(findUsersLikeQuery, (rs, rowNum) -> makeUser(rs, rowNum), film_id);

    }

    protected static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        Integer id = rs.getInt("user_id");
        String login = rs.getString("login");
        String name = rs.getString("name");
        String email = rs.getString("email");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(id,
                login,
                email,
                name,
                birthday);
    }
}
