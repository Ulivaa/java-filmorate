package ru.yandex.practicum.javafilmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.storage.LikeStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class RecommendationService {

    private final LikeStorage likeStorage;
    private final FilmService filmService;

    @Autowired
    public RecommendationService(LikeStorage likeStorage, FilmService filmService) {
        this.likeStorage = likeStorage;
        this.filmService = filmService;
    }

    public Collection<Film> getRecommendations(Integer userId) {
        var userLikes = likeStorage.allForUser(userId);
        if (userLikes.size() == 0) {
            return new HashSet<>(filmService.firstFilmsWithCountLike(10));
        }

        var allLikes = likeStorage.allExceptUser(userId);
        var map = new TreeMap<Integer, Set<Integer>>(Collections.reverseOrder());

        for (var u : allLikes.keySet()) {
            var anotherUserLikes = allLikes.get(u);
            var intersection = new HashSet<>(userLikes);
            intersection.retainAll(anotherUserLikes);
            var recommendations = new HashSet<>(anotherUserLikes);
            recommendations.removeAll(userLikes);
            map.put(intersection.size(), recommendations);
        }


        var result = map
                .values()
                .stream()
                .flatMap(Collection::stream)
                .map(filmService::findFilmById)
                .collect(Collectors.toUnmodifiableList());
        return result;
    }
}
