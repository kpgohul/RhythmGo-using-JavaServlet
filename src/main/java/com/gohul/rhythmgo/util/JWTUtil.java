package com.gohul.rhythmgo.util;

import com.gohul.rhythmgo.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Key;
import java.util.Date;

public class JWTUtil {

    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(
            "MY_SUPER_SECRET_KEY_FOR_SPOTIFY_256_BITS_1234".getBytes()
    );

    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 24 hrs

    // Create token
//    public static String generateToken(String email, int userId) {
//        return Jwts.builder()
//                .setSubject(email)
//                .claim("userId", userId)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
//                .signWith(SECRET_KEY)
//                .compact();
//    }
    public static String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("roles", user.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Validate + parse
    public static Jws<Claims> validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
    }

    public static String extractBearerToken(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

}

