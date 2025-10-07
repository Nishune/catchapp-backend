package com.catchapp.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Standardized error response model for the API.
 * Supports both single-error and field-based multiple errors.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String error;              // For single, generic errors
    private List<FieldError> errors;   // For validation / field-specific errors

    public ErrorResponse() {}

    public ErrorResponse(String error) {
        this.error = error;
    }

    public ErrorResponse(List<FieldError> errors) {
        this.errors = errors;
    }

    public static ErrorResponse of(String msg) {
        return new ErrorResponse(msg);
    }

    public static ErrorResponse of(List<FieldError> msgs) {
        return new ErrorResponse(msgs);
    }

    public String getError() {
        return error;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    /**
     * Nested class representing a single field error.
     */
    public static class FieldError {
        private String field;
        private String message;

        public FieldError() {}

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }
}
