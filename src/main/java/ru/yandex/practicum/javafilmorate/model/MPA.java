package ru.yandex.practicum.javafilmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MPA {
    G(1, "G"),
    PG(2, "PG"),
    PG_13(3, "PG-13"),
    R(4, "R"),
    NC_17(5, "NC-17");

    private int id;
    private String mpa;

    private MPA(int id, String mpa) {
        this.id = id;
        this.mpa = mpa;
    }


    public int getId() {
        return id;
    }

    public String getMpa() {
        return mpa;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMpa(String mpa) {
        this.mpa = mpa;
    }

    @JsonCreator
    public static MPA forValues(@JsonProperty("id") int id) {
        for (MPA obj : MPA.values()) {
            if (
                    Double.compare(obj.id, id) == 0) {
                return obj;
            }
        }
        return null;
    }
}

