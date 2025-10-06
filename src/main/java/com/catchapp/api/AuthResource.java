package com.catchapp.api;

import com.catchapp.api.dto.ErrorResponse;
import com.catchapp.api.dto.RegisterRequest;
import com.catchapp.api.dto.UserDto;
import com.catchapp.exception.ConflictException;
import com.catchapp.model.User;
import com.catchapp.service.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UserService userService;

    // Default constructor needed by JAX-RS
    public AuthResource() {}
    // Constructor for unit tests
    public AuthResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("/register")
    public Response register(@Valid RegisterRequest req) {
        try {
            User u = userService.register(req.getUsername(), req.getEmail(), req.getPassword());
            return Response.status(Response.Status.CREATED)
                    .entity(UserDto.from(u))
                    .build();
        } catch (ConflictException e) {

            return Response.status(Response.Status.CONFLICT)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();
        }
    }
}
