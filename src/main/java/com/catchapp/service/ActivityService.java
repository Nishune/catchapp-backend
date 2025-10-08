package com.catchapp.service;

import com.catchapp.model.Activity;
import com.catchapp.model.User;
import com.catchapp.repository.ActivityRepository;
import com.catchapp.repository.FavoriteRepository;
import com.catchapp.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ActivityService {

    @Inject
    ActivityRepository activityRepository;

    @Inject
    FavoriteRepository favoriteRepository;

    @Inject
    UserRepository userRepository;

    public ActivityService() {}
    public ActivityService(ActivityRepository repo, FavoriteRepository favRepo, UserRepository userRepo) {
        this.activityRepository = repo;
        this.favoriteRepository = favRepo;
        this.userRepository = userRepo;
    }
    public List<Activity> listActivities() {
        return activityRepository.findAll();
    }

    public Activity getActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));
    }

    public void likeActivity(String username, Long activityId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Activity already liked"));
    }
}
