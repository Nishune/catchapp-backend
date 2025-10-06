package com.catchapp.service;

import com.catchapp.model.User;
import com.catchapp.repository.UserRepository;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    @Inject
    UserRepository userRepository; // Dependency Injection of UserRepository

    // Constructor for testing purposes
    public UserService() {}
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

        public User register(String username, String email, String rawPassword) {
            validate(username, email, rawPassword);

            if (userRepository.findByUsername(username).isPresent()) {
                throw new ConflictException("Username already exists");
            }
            if (userRepository.findByEmail(email).isPresent()) {
                throw new ConflictException("Email already registered");
            }

            String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
            User u = new User(null, username, email, hash);
            return userRepository.save(u);
    }

    private void validate(String username, String email, String password) {
        if (isBlank(username) || isBlank(email) || isBlank(password)) {
            throw new IllegalArgumentException("Username, email, and password are required.");
        }
        if (username.length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters long.");
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
