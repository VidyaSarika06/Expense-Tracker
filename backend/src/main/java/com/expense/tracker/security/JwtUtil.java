package com.expense.tracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JwtUtil — central utility for all JWT operations.
 *
 * Responsibilities:
 *   - Generate a signed JWT token for a user
 *   - Extract claims (username, expiration) from a token
 *   - Validate a token against a UserDetails object
 *
 * Algorithm: HMAC-SHA256 (HS256) — symmetric key signing
 */
@Component
public class JwtUtil {

    // Read from application.properties: jwt.secret
    @Value("${jwt.secret}")
    private String secret;

    // Read from application.properties: jwt.expiration (milliseconds)
    @Value("${jwt.expiration}")
    private long expiration;

    // ── Token Generation ───────────────────────────────────────────────────────

    /**
     * Generates a JWT token for the given UserDetails.
     * The subject (username) is set to the user's email.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // You can add extra claims here later e.g. claims.put("role", ...)
        return buildToken(claims, userDetails.getUsername());
    }

    private String buildToken(Map<String, Object> extraClaims, String subject) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)                          // email as subject
                .setIssuedAt(new Date())                      // token creation time
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // expiry
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // sign with secret
                .compact();
    }

    // ── Token Validation ───────────────────────────────────────────────────────

    /**
     * Returns true if the token is valid:
     *   - subject matches the UserDetails username
     *   - token is not expired
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // ── Claims Extraction ──────────────────────────────────────────────────────

    // Extracts the email (subject) from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extracts the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic claim extractor — accepts any Claims → T function
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parses and returns all claims from the token
    // Throws JwtException if the token is invalid or tampered
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Returns true if the token's expiration date is before now
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ── Key Helper ─────────────────────────────────────────────────────────────

    // Converts the secret string into a cryptographic Key object for HMAC-SHA256
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
