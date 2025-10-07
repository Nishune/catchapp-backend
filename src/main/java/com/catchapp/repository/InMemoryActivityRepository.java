package com.catchapp.repository;

import com.catchapp.model.Activity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryActivityRepository implements ActivityRepository {

    private final Map<Long, Activity> activities = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public InMemoryActivityRepository() {
        // Data for development and testing purposes

        save(Activity.builder()
                .title("Filmkväll")
                .description("Se film med vänner")
                .date(LocalDate.now().plusDays(1))
                .location("Luleå")
                .imageUrl("film.jpg")
                .build());

        save(Activity.builder()
                .title("Löpning i parken")
                .description("5 km med lokalgrupp")
                .date(LocalDate.now().plusDays(2))
                .location("Piteå")
                .imageUrl("run.jpg")
                .build());

        save(Activity.builder()
                .title("Brädspelskväll")
                .description("Klassiska spel och fika")
                .date(LocalDate.now().plusDays(3))
                .location("Boden")
                .imageUrl("boardgames.jpg")
                .build());
    }

    @Override
    public List<Activity> findAll() {
        return new ArrayList<>(activities.values());
    }

    @Override
    public Optional<Activity> findById(Long id) {
        return Optional.ofNullable(activities.get(id));
    }

    @Override
    public Activity save(Activity activity) {
        Long id = (activity.getId() != null)
                ? activity.getId()
                : idGenerator.getAndIncrement();

        Activity toSave = Activity.builder()
                .id(id)
                .title(activity.getTitle())
                .description(activity.getDescription())
                .date(activity.getDate())
                .location(activity.getLocation())
                .imageUrl(activity.getImageUrl())
                .build();

        activities.put(id, toSave);
        return toSave;
    }
}
