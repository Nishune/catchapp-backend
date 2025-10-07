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

class RegisterRequestTest {

    private static Validator validator;
    private static ValidatorFactory factory;

    @BeforeAll
    static void setupValidator() {
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
    void validRegisterRequest() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("testuser"); // Valid username
        req.setEmail("test@example.com"); // Valid email
        req.setPassword("password123"); // Valid password

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(req); // Validate the request

        assertThat(violations).isEmpty(); // Expect no violations
    }

    @Test
    void invalidBlankUsername() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("");
        req.setEmail("test@example.com");
        req.setPassword("password123");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(req);
        assertThat(violations).hasSizeGreaterThanOrEqualTo(1); // Expect at least one violation
    }
    @Test
    void invalidShortUsername() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("ab"); // too short
        req.setEmail("test@example.com");
        req.setPassword("password123");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(req);
        assertThat(violations).hasSize(1);
    }
    @Test
    void invalidBlankPassword() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("testuser");
        req.setEmail("test@example.com");
        req.setPassword("");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(req);
        assertThat(violations).hasSizeGreaterThanOrEqualTo(1);
    }
}
