package com.plazoleta.infrastructure.output.security;

import com.plazoleta.application.port.output.TokenPort;
import com.plazoleta.domain.model.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtAdapter implements TokenPort {

    private final SecretKey key;
    private final long expirationMs;

    public JwtAdapter(String secret, long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    @Override
    public String generate(User user) {
        Date now = new Date();
        JwtBuilder builder = Jwts.builder()
                .subject(user.getCorreo())
                .claim("id", user.getId())
                .claim("rol", user.getRol().name());

        // Los empleados llevan en el token el restaurante donde trabajan.
        if (user.getIdRestaurante() != null) {
            builder.claim("idRestaurante", user.getIdRestaurante());
        }

        return builder
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(key)
                .compact();
    }
}
