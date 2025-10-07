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
        // mocks the UserRepository
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void registerShouldSaveUserWhenValidData() {
        // testing normal registration with correct data
        String username = "testuser";
        String email = "test@example.com";
        String password = "password123";

        // Simulate no existing user with same username or email
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Mock save() so it returns the user with an ID
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

        // runs the registration
        User savedUser = userService.register(username, email, password);

        // verifies the user was saved correctly
        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(savedUser.getUsername()).isEqualTo(username);
        assertThat(savedUser.getEmail()).isEqualTo(email);
        // Password should be hashed
        assertThat(savedUser.getPasswordHash()).isNotEqualTo(password);
        // Verify the password matches the hash
        assertThat(BCrypt.checkpw(password, savedUser.getPasswordHash())).isTrue();

        // verify that save was called.
        verify(userRepository).save(any(User.class));

    }

    @Test
    void registerShouldThrowConflictWhenUsernameExists() {
        // testing registration when username is already taken
        String username = "takenuser";
        String email = "new@example.com";
        String password = "password123";

        // Simulate existing user with same username
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(User.builder().build()));

        // controls the exception thrown
        assertThatThrownBy(() -> userService.register(username, email, password))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Username already exists");
    }

    @Test
    void registerShouldThrowConflictWhenEmailExists() {
        // Should throw ConflictException when email is already registered
        String username = "newuser";
        String email = "taken@example.com";
        String password = "password123";

        // Simulate existing user with same email
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(User.builder().build()));

        // controls the exception thrown
        assertThatThrownBy(() -> userService.register(username, email, password))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Email already registered");
    }

    @Test
    void authenticateShouldReturnUserWhenCredentialsValid() {
        // Testing successful authentication with correct credentials
        String username = "alex";
        String rawPassword = "password123";
        String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        User user = User.builder()
                .id(1L)
                .username(username)
                .passwordHash(hashed)
                .build();

        // Simulate finding the user by username
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // runs authentication
        User result = userService.authenticate(username, rawPassword);

        // verifies the returned user
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(username);
        verify(userRepository).findByUsername(username);
    }

    @Test
    void authenticateShouldThrowWhenUserNotFound() {
        // Should throw IllegalArgumentException when username does not exist
        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        // controls the exception thrown
        assertThatThrownBy(() -> userService.authenticate("missing", "any"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid username or password");
    }

    @Test
    void authenticateShouldThrowWhenPasswordIncorrect() {
        // Should throw IllegalArgumentException when password is incorrect
        String username = "alex";
        String hashed = BCrypt.hashpw("correct", BCrypt.gensalt());
        User user = User.builder()
                .id(1L)
                .username(username)
                .passwordHash(hashed)
                .build();

        // Simulate finding the user by username
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // controls the exception thrown
        assertThatThrownBy(() -> userService.authenticate(username, "wrong"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid username or password");
    }
    @Test
    void changePasswordShouldUpdateWhenOldPasswordCorrect() {
        String username = "alex";
        String oldPassword = "oldpass123";
        String newPassword = "newpass456";

        User user = User.builder()
                .username(username)
                .passwordHash(BCrypt.hashpw(oldPassword, BCrypt.gensalt()))
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        userService.changePassword(username, oldPassword, newPassword);

        verify(userRepository).findByUsername(username);
        verify(userRepository).save(user);
        assertThat(BCrypt.checkpw(newPassword, user.getPasswordHash())).isTrue();
    }


}
