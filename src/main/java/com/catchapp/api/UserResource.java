package com.catchapp.api;

import com.catchapp.api.dto.PasswordChangeRequest;
import com.catchapp.api.dto.PasswordChangeResponse;
import com.catchapp.security.JwtSecured;
import com.catchapp.service.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @PUT
    @Path("/password")
    @JwtSecured
    public Response changePassword(
            @Valid PasswordChangeRequest req,
            @Context SecurityContext securityContext
            ) {
        String username = (securityContext.getUserPrincipal() != null)
                ? securityContext.getUserPrincipal().getName()
                : null;

        if (username == null || username.isBlank()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new PasswordChangeResponse("Missing or invalid token"))
                    .build();
        }

        userService.changePassword(username, req.getOldPassword(), req.getNewPassword());
        return Response.ok(new PasswordChangeResponse("Password changed successfully")).build();
    }


}
