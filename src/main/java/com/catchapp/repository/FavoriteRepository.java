package com.catchapp.repository;

import com.catchapp.model.Activity;
import com.catchapp.model.Favorite;
import com.catchapp.model.User;

import java.util.List;

public interface FavoriteRepository {
    boolean existsByUserAndActivity(User user, Activity activity);
    Favorite save(Favorite favorite);
    void deleteByUserAndActivity(User user, Activity activity);
    List<Activity> findActivitiesByUser(User user);
}
