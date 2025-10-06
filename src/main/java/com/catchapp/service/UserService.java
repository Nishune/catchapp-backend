package com.catchapp.service;

import com.catchapp.exception.ConflictException;
import com.catchapp.model.User;
import com.catchapp.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository; // Dependency Injection of UserRepository

    // Constructor for testing purposes
    public UserService() {}
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

public User register(String username, String email, String rawPassword) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new ConflictException("Username already exists");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("Email already registered");
        }

        String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        User u = User.builder()
                .username(username)
                .email(email)
                .passwordHash(hash)
                .build();

        return userRepository.save(u);
}

}
