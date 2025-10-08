package com.catchapp.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {

        // Tillåt frontend att anropa backend (lokalt)
        res.getHeaders().putSingle("Access-Control-Allow-Origin", "http://localhost:4200");

        // Gör det möjligt att skicka cookies / auth headers
        res.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");

        // Tillåt nödvändiga headers (inkl. Authorization för JWT)
        res.getHeaders().putSingle("Access-Control-Allow-Headers",
                "Origin, Content-Type, Accept, Authorization, X-Requested-With");

        // Tillåt alla metoder som används i din app
        res.getHeaders().putSingle("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        // Valfritt men rekommenderat: ange hur länge en preflight kan cacheas
        res.getHeaders().putSingle("Access-Control-Max-Age", "3600");
    }
}
