package com.catchapp.service;

import com.catchapp.model.Activity;
import com.catchapp.repository.ActivityRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ActivityService {

    @Inject
    ActivityRepository activityRepository;

    public ActivityService() {}
    public ActivityService(ActivityRepository repo) {
        this.activityRepository = repo;
    }
    public List<Activity> listActivities() {
        return activityRepository.findAll();
    }

    public Activity getActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));
    }
}
