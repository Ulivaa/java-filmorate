package ru.yandex.practicum.javafilmorate.storage.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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
    public int save(Film film) {
        // старый функционал без возврата id
//        jdbcTemplate.update(saveQuery,
//                film.getName(),
//                film.getDescription(),
//                film.getReleaseDate(),
//                film.getDuration()
//                , film.getMpa().toString());

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        int film_id = (int)simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue();
        //реализовать жанры уже из существующих
//        if (!film.getGenre().isEmpty()){
//            saveGenre();
//        }
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

    private Collection<String> getGenres(int film_id) {
        return jdbcTemplate.query(getGenreQuery,
                (rs, rowNum) -> rs.getString("name"),
                film_id);
    }

   private void saveGenre(int film_id, Collection<String> genres){

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
