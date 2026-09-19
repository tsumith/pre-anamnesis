package com.sumith.jfs.PaAnaBot.config;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
public class JwtUtil {
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor("mySecretKeymySecretKeymySecretKey12".getBytes());
    private static final long EXPIRATION = 1000 * 60 * 60 * 5; 
    public static String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY) 
                .compact();
    }
    public static String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) 
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public static String getRoleFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
