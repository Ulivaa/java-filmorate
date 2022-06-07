package ru.yandex.practicum.javafilmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.javafilmorate.model.MPA;
import ru.yandex.practicum.javafilmorate.service.MpaService;

import java.util.Collection;

@Slf4j
@RestController
public class MpaController {
    MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/mpa/{id}")
    public MPA findMpaById(@PathVariable Integer id) {
        return mpaService.findMpaById(id);
    }

    @GetMapping("/mpa")
    public Collection<MPA> returnAllMpa() {
        return mpaService.returnAllMpa();
    }

}
