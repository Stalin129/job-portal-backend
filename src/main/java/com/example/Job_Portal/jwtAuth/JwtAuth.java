package com.example.Job_Portal.jwtAuth;

import javax.crypto.SecretKey;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

@Service
public class JwtAuth {
    private static final String SECRET =
            "mysecretkeystalinmysecretkey123456";

    private SecretKey getSignKey() {

        return Keys.hmacShaKeyFor(
                SECRET.getBytes()
        );
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60
                        )
                )
                .signWith(
                        getSignKey()
                )
                .compact();
    }

    public String extractToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build().parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }
}

