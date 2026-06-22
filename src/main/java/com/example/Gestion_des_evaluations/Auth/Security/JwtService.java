package com.example.Gestion_des_evaluations.Auth.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

// Service chargé de générer et vérifier les JWT
@Service
public class JwtService {

    // Clé secrète lue depuis application.properties
    @Value("${jwt.secret}")
    private String secret;

    // Durée de vie du token en millisecondes
    @Value("${jwt.expiration}")
    private long expiration;

    // Transforme la clé texte en clé cryptographique utilisable par JJWT
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    // Génère un token simple à partir de l'utilisateur connecté
    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails);
    }

    // Génère un token avec des claims supplémentaires si besoin
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims) // données ajoutées au token
                .setSubject(userDetails.getUsername()) // email ou username
                .setIssuedAt(new Date()) // date de création
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // date d'expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // signature du token
                .compact();
    }

    // Extrait le subject du token, ici l'email de l'utilisateur
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrait une information précise depuis les claims du token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Récupère tous les claims du token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Vérifie que le token correspond bien à l'utilisateur et n'est pas expiré
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Vérifie si le token est expiré
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}