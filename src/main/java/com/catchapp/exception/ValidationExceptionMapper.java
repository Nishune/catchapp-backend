package com.catchapp.exception;

import com.catchapp.api.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

/**
 * Maps bean validation errors to a structured JSON response
 * with field and message for each violation.
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<ErrorResponse.FieldError> fieldErrors = exception.getConstraintViolations().stream()
                .map(v -> new ErrorResponse.FieldError(
                        extractFieldName(v.getPropertyPath().toString()),
                        v.getMessage()
                ))
                .toList();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ErrorResponse.of(fieldErrors))
                .build();
    }

    /**
     * Utility method to clean up property path (e.g., "register.arg0.username" → "username").
     */
    private String extractFieldName(String path) {
        if (path == null || path.isBlank()) return "unknown";
        String[] parts = path.split("\\.");
        return parts[parts.length - 1];
    }
}
