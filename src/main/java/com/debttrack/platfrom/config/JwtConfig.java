package com.debttrack.platfrom.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Value("${jwt.config.secret}")
    private String SECRET;

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
}