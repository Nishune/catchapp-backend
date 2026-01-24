package com.catchapp.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")  // Apply to all URLs
public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req; // Cast to HttpServletRequest
        HttpServletResponse response = (HttpServletResponse) res; // Cast to HttpServletResponse

        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200"); // Adjust as needed
        response.setHeader("Access-Control-Allow-Credentials", "true"); // Allow cookies
        response.setHeader("Access-Control-Allow-Headers", // Allow specific headers
                "Origin, Content-Type, Accept, Authorization, X-Requested-With"); // Adjust as needed
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD"); // Allowed methods
        response.setHeader("Access-Control-Max-Age", "3600"); // Cache preflight response for 1 hour

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) { // Handle preflight requests
            response.setStatus(HttpServletResponse.SC_OK); // Respond with 200 OK
            return;
        }
        chain.doFilter(req, res);
    }
}
