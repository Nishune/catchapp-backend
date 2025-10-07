package com.catchapp.api;

import com.catchapp.api.dto.LoginRequest;
import com.catchapp.api.dto.LoginResponse;
import com.catchapp.api.dto.RegisterRequest;
import com.catchapp.exception.ConflictException;
import com.catchapp.model.User;
import com.catchapp.security.JwtUtil;
import com.catchapp.service.UserService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthResourceTest {

    private UserService userService;
    private AuthResource authResource;

    @BeforeEach
    void setUp() {
        // Mocking UserService to test AuthResource in isolation
        userService = mock(UserService.class);
        authResource = new AuthResource(userService);

    }

    @Test
    void registerShouldReturnCreatedWhenValidUser() {
        // When valid data is provided, should return 201 Created
        RegisterRequest req = new RegisterRequest();
        req.setUsername("alex");
        req.setEmail("alex@example.com");
        req.setPassword("password123");

        // Mock the userService to return a User object when register is called
        User user = User.builder()
                .id(1L)
                .username("alex")
                .email("alex@example.com")
                .passwordHash("hash")
                .build();

        when(userService.register(anyString(), anyString(), anyString()))
                .thenReturn(user);

        // Call the register method (endpoint)
        Response response = authResource.register(req);

        // Verify the response
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        // Verify that correct data was passed to service-layer
        verify(userService).register("alex", "alex@example.com", "password123");
    }

    @Test
    void registerShouldThrowConflictExceptionWhenUsernameExists() {
        // Testing if the username already exists, then a ConflictException is thrown
        RegisterRequest req = new RegisterRequest();
        req.setUsername("alex");
        req.setEmail("alex@example.com");
        req.setPassword("password123");

        // simulate that user service finds a conflict
        when(userService.register(anyString(), anyString(), anyString()))
                .thenThrow(new ConflictException("Username already exists"));

        // except AuthResource to throw the same exception
        ConflictException thrown = assertThrows(
                ConflictException.class,
                () -> authResource.register(req)
        );

        // verify the exception message
        assertThat(thrown.getMessage()).isEqualTo("Username already exists");
        verify(userService).register("alex", "alex@example.com", "password123");
    }

    @Test
    void registerShouldThrowIllegalArgumentExceptionWhenInvalidData() {
        // Testing if invalid data is provided, then an IllegalArgumentException is thrown
        RegisterRequest req = new RegisterRequest();
        req.setUsername(""); // invalid
        req.setEmail("not-an-email");
        req.setPassword(""); // invalid

        // Simulate that userService throws IllegalArgumentException for invalid data
        when(userService.register(anyString(), anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        // Expect AuthResource to throw the same exception
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> authResource.register(req)
        );

        // Verify the exception message
        assertThat(thrown.getMessage()).isEqualTo("Invalid data");
        verify(userService).register(anyString(), anyString(), anyString());
    }

    @Test
    void loginShouldReturnTokenWhenCredentialsValid() {
        // Testing successful login returns JWT token
        LoginRequest req = new LoginRequest();
        req.setUsername("alex");
        req.setPassword("password123");

        // simulate existing user
        User user = User.builder()
                .id(1L)
                .username("alex")
                .passwordHash("hashed")
                .build();

        // mock authenticate() to succeed
        when(userService.authenticate("alex", "password123")).thenReturn(user);

        // Mocking JwtUtil to not generate a real token in tests
        try (var mockedJwt = mockStatic(JwtUtil.class)) {
            mockedJwt.when(() -> JwtUtil.generateToken("alex"))
                    .thenReturn("fake-jwt-token");

            // Call the login method (endpoint)
            Response response = authResource.login(req);

            // Verify the response
            assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
            assertThat(response.getEntity()).isInstanceOf(LoginResponse.class);

            // Verify that correct data is returned
            LoginResponse body = (LoginResponse) response.getEntity();
            assertThat(body.getToken()).isEqualTo("fake-jwt-token");
            assertThat(body.getUserId()).isEqualTo(1L);
            assertThat(body.getUsername()).isEqualTo("alex");

            // Verify that userService was called
            verify(userService).authenticate("alex", "password123");
        }
    }

    @Test
    void loginShouldThrowIllegalArgumentExceptionWhenCredentialsInvalid() {
        // Testing login with invalid credentials throws IllegalArgumentException
        LoginRequest req = new LoginRequest();
        req.setUsername("alex");
        req.setPassword("wrong");

        // simulate that authentication fails
        when(userService.authenticate("alex", "wrong"))
                .thenThrow(new IllegalArgumentException("Invalid username or password"));

        // Expect AuthResource to throw the same exception
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> authResource.login(req)
        );

        // Verify the exception message
        assertThat(thrown.getMessage()).isEqualTo("Invalid username or password");
        verify(userService).authenticate("alex", "wrong");
    }



}