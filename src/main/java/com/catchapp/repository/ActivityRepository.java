package com.catchapp.repository;

import com.catchapp.model.Activity;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository {
    List<Activity> findAll();
    Optional<Activity> findById(Long id);
    Activity save(Activity activity);
}
