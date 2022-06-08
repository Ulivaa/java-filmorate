package ru.yandex.practicum.javafilmorate.storage.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.GENRE;
import ru.yandex.practicum.javafilmorate.model.MPA;
import ru.yandex.practicum.javafilmorate.storage.FilmStorage;
import ru.yandex.practicum.javafilmorate.storage.ReadFilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("FilmDbStorage")
@Primary
public class FilmDbStorage implements FilmStorage, ReadFilmStorage {

    private final String deleteFilm = "DELETE FROM films WHERE film_id = ?";
    private final String deleteFilmGenres = "DELETE FROM films_genre WHERE film_id = ?";
    private final String updateQuery = "update films set name = ?, description = ?, release_date = ?, duration = ?,  mpa = ? where film_id =? ";
    private final String findByIdQuery = "select * from films where film_id = ?";
    private final String getGenreQuery = "SELECT g.name from genres g JOIN films_genre f on g.genre_id = f.genre_id WHERE film_id = ?";
    private final String getUserFilms = "select DISTINCT * from films f join likes l on f.film_id = l.film_id where user_id = ?";
    private final String saveGenreQuery = "insert into films_genre(film_id, genre_id) values(?, ?)";
    private final String getPopularQuery = "SELECT f.film_id, f.name, f.description, f.release_date,f.duration, f.mpa\n" +
            "FROM films AS f LEFT JOIN likes AS l on f.film_id = l.film_id\n" +
            "GROUP BY f.film_id\n" +
            "ORDER BY COUNT(DISTINCT l.user_id) DESC LIMIT ?";
    private final String searchQuery = "select f.*, count(l.film_id) as cnt from films f " +
            "left join likes l on f.film_id = l.film_id " +
            "where f.name ilike ? " +
            "group by f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA " +
            "order by cnt desc";


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
        if (film.getGenres() != null) {
            saveGenre(film_id, film.getGenres());
        }
        return film_id;
    }

    @Override
    public void delete(Integer film_id) {
        jdbcTemplate.update(deleteFilm,
                film_id);
    }

    public void update(Film film) {
        jdbcTemplate.update(updateQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().toString(),
                film.getId());
        if (film.getGenres() != null) {
            if (!film.getGenres().isEmpty()) {
                deleteGenre(film.getId());
                saveGenre(film.getId(), film.getGenres());
            } else {
                deleteGenre(film.getId());
            }
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


    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        int film_id = rs.getInt("film_id");

        return new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getShort("duration"),
                userDbStorage.findUsersLikeToFilm(film_id),
                MPA.valueOf(rs.getString("mpa")),
                getGenres(film_id)
//                new HashSet<GENRE>()
        );
    }

    public Collection<Film> getPopular(int limit) {
        return jdbcTemplate.query(getPopularQuery, ((rs, rowNum) -> makeFilm(rs, rowNum)), limit);
    }

    private Collection<Film> getUserFilms(int user_id){
        return jdbcTemplate.query(getUserFilms, ((rs, rowNum) -> makeFilm(rs, rowNum)), user_id);
    }

    @Override
    public List<Film> getCommonFilms(int user_id, int friend_id) {
        Collection<Film> userF = getUserFilms(user_id);
        Collection<Film> friendF = getUserFilms(friend_id);
        return userF.stream()
                .filter(o -> friendF.contains(o)).sorted((o1, o2) -> o2.getUsersLike().size()- o1.getUsersLike().size())
                .collect(Collectors.toList());
    }

    private Collection<GENRE> getGenres(int film_id) {
        Collection<GENRE> genres = jdbcTemplate.query(getGenreQuery,
                (rs, rowNum) -> makeGenre(rs, rowNum),
                film_id);
        if (!genres.isEmpty()) {
            return genres;
        } else {
            return null;
        }
    }

    private void deleteGenre(Integer film_id) {
        jdbcTemplate.update(deleteFilmGenres,
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
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from films_genre where film_id = ? AND genre_id = ?", film_id, genre_id);
        if (genreRows.next()) {
            return true;
        } else {
            return false;
        }
    }

    private void saveGenre(int film_id, Collection<GENRE> genres) {
        for (GENRE genre : genres) {
            if (findGenre(genre.getId())) {
                if (!findGenreForFilm(genre.getId(), film_id)) {
                    jdbcTemplate.update(saveGenreQuery,
                            film_id,
                            genre.getId());
                }
            }
        }
    }

    private GENRE makeGenre(ResultSet rs, int rowNum) throws SQLException {
        String name = rs.getString("name");
        GENRE genre = GENRE.valueOf(name);
        return genre;
    }

    @Override
    public List<Film> search(String query) {
        return jdbcTemplate.query(searchQuery, this::makeFilm, "%" + query + "%");
    }
}
