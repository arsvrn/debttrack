package com.debttrack.platfrom.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    private final String SECRET = "your-256-bit-secret-your-256-bit-secret";

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
}