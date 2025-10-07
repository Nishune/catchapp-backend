package com.catchapp.api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {

    private static Validator validator;
    private static ValidatorFactory factory;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    void validLoginRequest() {
        LoginRequest req = new LoginRequest();
        req.setUsername("alex");
        req.setPassword("password123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(req);
        assertThat(violations).isEmpty(); // Expect no validation errors
    }

    @Test
    void invalidBlankUsername() {
        LoginRequest req = new LoginRequest();
        req.setUsername(""); // blank
        req.setPassword("password123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(req);
        assertThat(violations).isNotEmpty();
    }
    @Test
    void invalidShortUsername() {
        LoginRequest req = new LoginRequest();
        req.setUsername("ab"); // too short
        req.setPassword("password123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(req);
        assertThat(violations).isNotEmpty();
    }
    @Test
    void invalidLongUsername() {
        LoginRequest req = new LoginRequest();
        req.setUsername("a".repeat(51)); // 51 chars
        req.setPassword("password123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(req);
        assertThat(violations).isNotEmpty();
    }

    @Test
    void invalidBlankPassword() {
        LoginRequest req = new LoginRequest();
        req.setUsername("alex");
        req.setPassword(""); // blank

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(req);
        assertThat(violations).isNotEmpty();
    }
    @Test
    void invalidShortPassword() {
        LoginRequest req = new LoginRequest();
        req.setUsername("alex");
        req.setPassword("123"); // too short

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(req);
        assertThat(violations).isNotEmpty();
    }
    @Test
    void invalidTooLongPassword() {
        LoginRequest req = new LoginRequest();
        req.setUsername("alex");
        req.setPassword("a".repeat(101)); // too long

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(req);
        assertThat(violations).isNotEmpty();
    }
}
