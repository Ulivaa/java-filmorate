package ru.yandex.practicum.javafilmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GENRE {
    COMEDY(1, "Комедия"),
    DRAMA(2, "Драма"),
    CARTOON(3, "Мультфильм"),
    ACTION(4, "Боевик"),
    MELODRAMA(5, "Мелодрама"),
    DOCUMENTARY(6, "Документальный");

    private int id;
    private String genre;

     GENRE(int id, String genre) {
        this.id = id;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return genre;
    }

    public void setName(String genre) {
        this.genre = genre;
    }

    @JsonCreator
    public static GENRE forValues(@JsonProperty("id") int id) {
        for (GENRE obj : GENRE.values()) {
            if (
                    Double.compare(obj.id, id) == 0) {
                return obj;
            }
        }
        return null;
    }
}






