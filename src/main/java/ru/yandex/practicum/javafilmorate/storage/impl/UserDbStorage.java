package ru.yandex.practicum.javafilmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Component("UserDbStorage")
@Repository
public class UserDbStorage implements UserStorage {

    JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        String sqlQuery = "insert into users(login, name, email, birthday) values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                Date.valueOf(user.getBirthday()));
    }

    @Override
    public void update(User user) {
        String sqlQuery = "update users set login = ?, name = ?, email = ?, birthday = ? where user_id =? ";
        jdbcTemplate.update(sqlQuery,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                Date.valueOf(user.getBirthday()),
                user.getId());
    }

    @Override
    public Optional<User> findUserById(int id) {
        String sql = "select * from users where user_id = ?";
        return jdbcTemplate.query(
                sql, (rs, rowNum) -> makeUser(rs, rowNum), id
        ).stream().findAny();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        String sql = "select * from users where email = ?";
        return jdbcTemplate.query(
                sql, (rs, rowNum) -> makeUser(rs, rowNum), email
        ).stream().findAny();
    }

    @Override
    public Collection<User> returnAllUsers() {
        return jdbcTemplate.query("SELECT * from users", (rs, rowNum) -> makeUser(rs, rowNum));
    }

    public Collection<User> findUsersLikeToFilm(Integer film_id) {
        String sql = "SELECT * from users u JOIN likes l on u.user_id = l.user_id WHERE film_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs, rowNum), film_id);

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
