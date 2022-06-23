package ru.yandex.practicum.javafilmorate.model;

import lombok.Getter;

@Getter
public enum SearchType {
    DIRECTOR("director"), TITLE("title");

    private final String type;

    SearchType(String type) {
        this.type = type;
    }

}
