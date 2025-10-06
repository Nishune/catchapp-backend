package com.catchapp.service;

import com.catchapp.exception.ConflictException;
import com.catchapp.model.User;
import com.catchapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void registerShouldSaveUserWhenValidData() {
        String username = "testuser";
        String email = "test@example.com";
        String password = "password123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            return User.builder()
                    .id(1L)
                    .username(u.getUsername())
                    .email(u.getEmail())
                    .passwordHash(u.getPasswordHash())
                    .createdAt(u.getCreatedAt())
                    .build();
        });

        User savedUser = userService.register(username, email, password);

        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(savedUser.getUsername()).isEqualTo(username);
        assertThat(savedUser.getEmail()).isEqualTo(email);
        assertThat(savedUser.getPasswordHash()).isNotEqualTo(password); // Should be hashed
        assertThat(BCrypt.checkpw(password, savedUser.getPasswordHash())).isTrue();

        verify(userRepository).save(any(User.class));

    }

    @Test
    void register_ShouldThrowConflict_WhenUsernameExists() {
        String username = "takenuser";
        String email = "new@example.com";
        String password = "password123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(User.builder().build()));

        assertThatThrownBy(() -> userService.register(username, email, password))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Username already exists");
    }

    @Test
    void register_ShouldThrowConflict_WhenEmailExists() {
        String username = "newuser";
        String email = "taken@example.com";
        String password = "password123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(User.builder().build()));

        assertThatThrownBy(() -> userService.register(username, email, password))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Email already registered");
    }
}
