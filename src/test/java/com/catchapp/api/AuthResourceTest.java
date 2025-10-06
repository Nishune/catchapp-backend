package com.catchapp.api;

import com.catchapp.api.dto.RegisterRequest;
import com.catchapp.exception.ConflictException;
import com.catchapp.model.User;
import com.catchapp.service.UserService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthResourceTest {

    private UserService userService;
    private AuthResource authResource;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        authResource = new AuthResource(userService);
    }

    @Test
    void registerShouldReturnCreatedWhenValidRequest() {

        RegisterRequest req = new RegisterRequest();
        req.setUsername("testuser");
        req.setEmail("testuser@example.com");
        req.setPassword("testpassword");

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .passwordHash("hashedpassword")
                .build();

        when(userService.register("testuser", "testuser@example.com", "testpassword"))
                .thenReturn(user);

        Response response = authResource.register(req);

        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(response.getEntity()).isNotNull();
    }

    @Test
    void registerShouldReturn409WhenUsernameConflict() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("testuser");
        req.setEmail("testuser@example.com");
        req.setPassword("testpassword");

        when(userService.register(req.getUsername(), req.getEmail(), req.getPassword()))
                .thenThrow(new ConflictException("Username already exists"));

        Response response = authResource.register(req);

        assertThat(response.getStatus()).isEqualTo(Response.Status.CONFLICT.getStatusCode());
    }

    @Test
    void register_ShouldReturn400_WhenInvalidData() {
        RegisterRequest req = new RegisterRequest(); // empty -> invalid

        when(userService.register(req.getUsername(), req.getEmail(), req.getPassword()))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        Response response = authResource.register(req);

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

}
