package com.example.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

public class JwtUtils {
    private static final String PREFIX = "Bearer ";
    private static final String SECRET = "1234";
    private static final int EXPIRATION = 60;// minutos

    public static String generateJWT(UserDetails userDetails){
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withClaim("role", userDetails.getAuthorities().stream()
                        .map(Authority -> Authority.getAuthority())
                        .collect(Collectors.toList()))
                .withExpiresAt(Instant.now().plus(Duration.ofMinutes(EXPIRATION)))
                .sign(Algorithm.HMAC512(SECRET));
    }
    public static void validate(String token){
        JWT.require(Algorithm.HMAC512(SECRET))
                .build()
                .verify(token);
    }
    public static String getUsername(String token){
        return JWT.decode(token).getSubject();
    }
    public static String getRole(String token){
        return JWT.decode(token).getClaim("role").asString();
    }


}
