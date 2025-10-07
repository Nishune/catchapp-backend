package com.catchapp.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.security.Principal;

@Provider
@JwtSecured
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(ctx.getMethod())) return;

        String auth = ctx.getHeaderString("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            abort(ctx, 401, "Missing or invalid Authorization header");
            return;
        }

        String token = auth.substring("Bearer ".length()).trim();

        try {
            String username = JwtUtil.validateAndGetSubject(token);


            SecurityContext current = ctx.getSecurityContext();
            ctx.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> username;
                }

                @Override
                public boolean isUserInRole(String role) {
                    return false;
                }

                @Override
                public boolean isSecure() {
                    return current != null && current.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Bearer";
                }
            });

        } catch (SecurityException e) {
            abort(ctx, 401, e.getMessage());
        }
    }

    private void abort(ContainerRequestContext ctx, int status, String msg) {
        ctx.abortWith(Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity("{\"error\":\"" + msg + "\"}")
                .build());
    }
}
