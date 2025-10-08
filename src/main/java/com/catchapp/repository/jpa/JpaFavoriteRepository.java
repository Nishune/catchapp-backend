package com.catchapp.repository.jpa;

import com.catchapp.model.Activity;
import com.catchapp.model.Favorite;
import com.catchapp.model.User;
import com.catchapp.repository.FavoriteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
@Alternative
public class JpaFavoriteRepository implements FavoriteRepository {

    @PersistenceContext(unitName = "catchappPU")
    EntityManager em;

    @Override
    public boolean existsByUserAndActivity(User user, Activity activity) {
        Long count = em.createQuery(
                "SELECT COUNT(f) FROM Favorite f WHERE f.user = :u AND f.activity = :a", Long.class)
                .setParameter("u", user)
                .setParameter("a", activity)
                .getSingleResult();
        return count != null && count > 0;
    }
    @Override
    public Favorite save(Favorite favorite) {
        if (favorite.getId() == null) {
            em.persist(favorite);
            return favorite;
        } else {
            return em.merge(favorite);
        }
    }

    @Override
    public void deleteByUserAndActivity(User user, Activity activity) {
        em.createQuery("DELETE FROM Favorite f WHERE f.user = :u AND f.activity = :a")
                .setParameter("u", user)
                .setParameter("a", activity)
                .executeUpdate();
    }

    @Override
    public List<Activity> findActivitiesByUser(User user) {
        return em.createQuery(
                        "SELECT f.activity FROM Favorite f WHERE f.user = :u ORDER BY f.createdAt DESC", Activity.class)
                .setParameter("u", user)
                .getResultList();
    }

}
