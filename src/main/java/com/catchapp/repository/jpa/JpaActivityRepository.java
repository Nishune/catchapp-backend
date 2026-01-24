package com.catchapp.repository.jpa;

import com.catchapp.model.Activity;
import com.catchapp.repository.ActivityRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Alternative
public class JpaActivityRepository implements ActivityRepository {

    @PersistenceContext(unitName = "catchappPU")
    EntityManager em;

    @Override
    public List<Activity> findAll() {
        return em.createQuery("SELECT a FROM Activity a ORDER BY a.date ASC", Activity.class)
                .getResultList();
    }

    @Override
    public Optional<Activity> findById(Long id) {
        return Optional.ofNullable(em.find(Activity.class, id));
    }

    @Override
    public Activity save(Activity activity) {
        if (activity.getId() == null) {
            em.persist(activity);
            return activity;
        } else {
            return em.merge(activity);
        }
    }
}
