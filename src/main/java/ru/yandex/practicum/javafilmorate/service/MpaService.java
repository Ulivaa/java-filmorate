package ru.yandex.practicum.javafilmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.javafilmorate.model.MPA;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MpaService {

    public MPA findMpaById(Integer id) {
        for (MPA obj : MPA.values()) {
            if (Double.compare(obj.getId(), id) == 0) {
                return obj;
            }
        }
        throw new MpaNotFoundException(String.format("MPA c id № %d не найден", id));
    }

    public Collection<MPA> returnAllMpa() {
        Collection<MPA> mpas = new HashSet<>();
        for (MPA obj : MPA.values()) {
            mpas.add(obj);


        }
        return mpas.stream().sorted((o, o2) -> o.getId() - o2.getId()).collect(Collectors.toList());

    }

}
