package ru.yandex.practicum.javafilmorate.storage.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.MPA;
import ru.yandex.practicum.javafilmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component("FilmDbStorage")
@Repository
@Primary
public class FilmDbStorage implements FilmStorage {

    private final String saveQuery = "insert into films(name, description, release_date, duration, mpa) values (?, ?, ?, ?, ?)";
    private final String updateQuery = "update films set name = ?, description = ?, release_date = ?, duration = ?,  mpa = ? where film_id =? ";
    private final String findByIdQuery = "select * from films where film_id = ?";
    private final String getGenreQuery = "SELECT g.name from genres g JOIN films_genre f on g.genre_id = f.genre_id WHERE film_id = ?";
    private final String getPopularQuery = "SELECT f.film_id, f.name, f.description, f.release_date,f.duration, f.mpa\n" +
            "FROM films AS f LEFT JOIN likes AS l on f.film_id = l.film_id\n" +
            "GROUP BY f.film_id\n" +
            "ORDER BY COUNT(DISTINCT l.user_id) DESC LIMIT ?";


    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public void save(Film film) {
        jdbcTemplate.update(saveQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()
                , film.getMpa().toString());
    }

    public void update(Film film) {
        jdbcTemplate.update(updateQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().toString(),
                film.getId());
    }

    @Override
    public Collection<Film> returnAllFilms() {
        return jdbcTemplate.query("SELECT * from films", (rs, rowNum) -> makeFilm(rs, rowNum));
    }

    public Film findFilmById(int film_id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(findByIdQuery, film_id);

        if (filmRows.next()) {

            Film film = new Film(
                    filmRows.getInt("film_id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getShort("duration"),
                    userDbStorage.findUsersLikeToFilm(film_id),
                    MPA.valueOf(filmRows.getString("mpa")),
                    getGenres(film_id));
            return film;
        }
        return null;
    }

    private Collection<String> getGenres(int film_id) {
        return jdbcTemplate.query(getGenreQuery,
                (rs, rowNum) -> rs.getString("name"),
                film_id);
    }


    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Integer film_id = rs.getInt("film_id");

        return new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getShort("duration"),
                userDbStorage.findUsersLikeToFilm(film_id),
                MPA.valueOf(rs.getString("mpa")),
                getGenres(film_id));
    }

    public Collection<Film> getPopular(int limit) {
        return jdbcTemplate.query(getPopularQuery, ((rs, rowNum) -> makeFilm(rs, rowNum)), limit);
    }
}
