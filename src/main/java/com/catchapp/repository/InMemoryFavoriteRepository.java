package com.catchapp.repository;

import com.catchapp.model.Activity;
import com.catchapp.model.Favorite;
import com.catchapp.model.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class InMemoryFavoriteRepository implements FavoriteRepository {

    private final Map<Long, Favorite> favorites = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public boolean existsByUserAndActivity(User user, Activity activity) {
        return favorites.values().stream()
                .anyMatch(f -> f.getUser().getUsername().equals(user.getUsername())
                        && f.getActivity().getId().equals(activity.getId()));
    }

    @Override
    public Favorite save(Favorite favorite) {
        Long id = favorite.getId() != null ? favorite.getId() : idGenerator.getAndIncrement();
        Favorite toSave = Favorite.builder()
                .id(id)
                .user(favorite.getUser())
                .activity(favorite.getActivity())
                .createdAt(favorite.getCreatedAt())
                .build();
        favorites.put(id, toSave);
        return toSave;
    }

    @Override
    public void deleteByUserAndActivity(User user, Activity activity) {
        favorites.entrySet().removeIf(e ->
                e.getValue().getUser().getUsername().equals(user.getUsername())
        && e.getValue().getActivity().getId().equals(activity.getId()));
    }

    @Override
    public List<Activity> findActivitiesByUser(User user) {
        return favorites.values().stream()
                .filter(f -> f.getUser().getUsername().equals(user.getUsername()))
                .map(Favorite::getActivity)
                .collect(Collectors.toList());
    }

}
