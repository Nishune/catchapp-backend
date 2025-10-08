package com.catchapp.exception;

import com.catchapp.api.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException e) {
        String message = e.getMessage();

        int status = (message != null && message.toLowerCase().contains("not found"))
                ? Response.Status.NOT_FOUND.getStatusCode()
                : Response.Status.BAD_REQUEST.getStatusCode();

        return Response.status(status)
                .entity(ErrorResponse.of(message))
                .build();
    }
}
