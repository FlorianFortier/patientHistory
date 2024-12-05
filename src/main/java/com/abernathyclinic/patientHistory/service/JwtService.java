package com.abernathyclinic.patientHistory.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service for managing JSON Web Tokens (JWT) in the application.
 * <p>
 * This service provides methods to generate, validate, and extract information
 * from JWTs using a secret key and an expiration configuration.
 * </p>
 */
@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Sets the secret key for signing and verifying JWTs.
     *
     * @param secretKey the secret key as a string.
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Sets the expiration time for generated JWTs.
     *
     * @param jwtExpiration the expiration time in milliseconds.
     */
    public void setJwtExpiration(long jwtExpiration) {
        this.jwtExpiration = jwtExpiration;
    }

    /**
     * Extracts the username (subject) from a given JWT.
     *
     * @param token the JWT to parse.
     * @return the username (subject) contained in the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from a given JWT using a resolver function.
     *
     * @param token          the JWT to parse.
     * @param claimsResolver a function to resolve the desired claim from the {@link Claims}.
     * @param <T>            the type of the claim to extract.
     * @return the extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT for a given user without additional claims.
     *
     * @param userDetails the user details for whom the token is generated.
     * @return a JWT as a string.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT for a given user with additional claims.
     *
     * @param extraClaims additional claims to include in the token.
     * @param userDetails the user details for whom the token is generated.
     * @return a JWT as a string.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Returns the configured JWT expiration time.
     *
     * @return the expiration time in milliseconds.
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Validates a JWT to ensure it is correctly signed and not expired.
     *
     * @param token the JWT to validate.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if a JWT is expired.
     *
     * @param token the JWT to check.
     * @return {@code true} if the token is expired, {@code false} otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from a JWT.
     *
     * @param token the JWT to parse.
     * @return the expiration date of the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from a JWT.
     *
     * @param token the JWT to parse.
     * @return the {@link Claims} contained in the token.
     * @throws IllegalArgumentException if the token is null or empty.
     */
    private Claims extractAllClaims(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Decode argument cannot be null or empty");
        }
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .setAllowedClockSkewSeconds(60)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Retrieves the signing key for JWT operations.
     *
     * @return a {@link Key} used for signing and verifying JWTs.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Builds a JWT with specified claims, user details, and expiration time.
     *
     * @param extraClaims additional claims to include in the token.
     * @param userDetails the user details for whom the token is generated.
     * @param expiration  the expiration time in milliseconds.
     * @return a JWT as a string.
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
