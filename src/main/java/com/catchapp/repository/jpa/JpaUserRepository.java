package com.catchapp.repository.jpa;

import com.catchapp.model.User;
import com.catchapp.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

@ApplicationScoped
@Alternative
public class JpaUserRepository implements UserRepository {

    @PersistenceContext(unitName = "catchappPU")
    EntityManager em;

    @Override
    public Optional<User> findByUsername(String username) {
        var list = em.createQuery("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:u)", User.class)
                .setParameter("u", username)
                .setMaxResults(1)
                .getResultList();
        return list.stream().findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        var list = em.createQuery("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:e)", User.class)
                .setParameter("e", email)
                .setMaxResults(1)
                .getResultList();
        return list.stream().findFirst();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            em.persist(user);
            return user;
        } else {
            return em.merge(user);
        }
    }

}
