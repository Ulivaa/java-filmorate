package ru.yandex.practicum.javafilmorate.storage.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.MPA;
import ru.yandex.practicum.javafilmorate.storage.FilmStorage;
import ru.yandex.practicum.javafilmorate.storage.ReadFilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Repository("FilmDbStorage")
@Primary
public class FilmDbStorage implements FilmStorage, ReadFilmStorage {

    private final String saveFilmQuery = "insert into films(name, description, release_date, duration, mpa) values (?, ?, ?, ?, ?)";
    private final String updateQuery = "update films set name = ?, description = ?, release_date = ?, duration = ?,  mpa = ? where film_id =? ";
    private final String findByIdQuery = "select * from films where film_id = ?";
    private final String getGenreQuery = "SELECT g.genre_id from genres g JOIN films_genre f on g.genre_id = f.genre_id WHERE film_id = ?";
    private final String saveGenreQuery = "insert into films_genre(film_id, genre_id) values(?, ?)";
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
    public int save(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        int film_id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        if (film.getGenre() != null) {
            saveGenre(film_id, film.getGenre());
        }
        return film_id;
    }

    public void update(Film film) {
        jdbcTemplate.update(updateQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().toString(),
                film.getId());
        if (film.getGenre() != null) {
            saveGenre(film.getId(), film.getGenre());
        }
    }

    @Override
    public Collection<Film> returnAllFilms() {
        return jdbcTemplate.query("SELECT * from films", (rs, rowNum) -> makeFilm(rs, rowNum));
    }

    public Optional<Film> findFilmById(int film_id) {
        return jdbcTemplate.query(
                findByIdQuery, (rs, rowNum) -> makeFilm(rs, rowNum), film_id
        ).stream().findAny();
    }

    private Collection<Integer> getGenres(int film_id) {
        return jdbcTemplate.query(getGenreQuery,
                (rs, rowNum) -> rs.getInt("genre_id"),
                film_id);
    }

    private boolean findGenre(Integer id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from genres where genre_id = ?", id);
        if (genreRows.next()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean findGenreForFilm(Integer genre_id, Integer film_id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from film_genres where genre_id = ? AND film_id", genre_id, film_id);
        if (genreRows.next()) {
            return true;
        } else {
            return false;
        }
    }

    private void saveGenre(int film_id, Collection<Integer> genres) {
        for (int genre : genres) {
            if (findGenre(genre)) {
                if (!findGenreForFilm(genre, film_id)) {
                    jdbcTemplate.update(saveGenreQuery,
                            film_id,
                            genre);
                }
            }
        }
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
