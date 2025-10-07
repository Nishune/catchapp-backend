package com.catchapp.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class JwtUtil {

    private static final long EXPIRATION_TIME = 3600_000; // 1 hour in milliseconds


    private static Key signingKey() {
        String secret = System.getenv("JWT_SECRET");
        if (secret == null || secret.isBlank()) {
            secret = System.getProperty("JWT_SECRET", "");
        }
        if (secret.isBlank()) {
            throw new IllegalStateException("JWT_SECRET is not configured");
        }

        byte[] keyBytes;
        if (isBase64(secret)) {
            keyBytes = Base64.getDecoder().decode(secret);
        } else {
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT_SECRET must be at least 256 bits");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static boolean isBase64(String secret) {
        try { Base64.getDecoder().decode(secret); return true; } catch (IllegalArgumentException e) { return false; }
    }

    public static String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(signingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String validateAndGetSubject(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            throw new SecurityException("Invalid or expired token", e);
        }
    }
}
