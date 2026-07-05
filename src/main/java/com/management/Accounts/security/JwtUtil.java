    package com.management.Accounts.security;
    
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.security.Keys;
    import org.springframework.stereotype.Component;
    
    import javax.crypto.SecretKey;
    import java.nio.charset.StandardCharsets;
    import java.util.Date;
    
    @Component
    public class JwtUtil {
    
        private final String SECRET =
                "my-secret-key-my-secret-key-my-secret-key";
    
        public String generateToken(String username) {
    
            SecretKey key = Keys.hmacShaKeyFor(
                    SECRET.getBytes(StandardCharsets.UTF_8)
            );
    
            return Jwts.builder()
                    .subject(username)
                    .issuedAt(new Date())
                    .expiration(
                            new Date(
                                    System.currentTimeMillis()
                                            + 1000 * 60 * 60 * 24 // 24 hours
                            )
                    )
                    .signWith(key)
                    .compact();
        }
        public boolean validateToken(String token) {
    
            SecretKey key = Keys.hmacShaKeyFor(
                    SECRET.getBytes(StandardCharsets.UTF_8)
            );
    
            try {
                Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token);
    
                return true;
            }
            catch (Exception e) {
                return false;
            }
        }
        public String extractUsername(String token) {
    
            SecretKey key = Keys.hmacShaKeyFor(
                    SECRET.getBytes(StandardCharsets.UTF_8)
            );
    
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        }
    }