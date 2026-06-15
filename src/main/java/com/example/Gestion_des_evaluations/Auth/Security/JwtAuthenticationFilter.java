package com.example.Gestion_des_evaluations.Auth.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Filtre exécuté à chaque requête pour vérifier si un JWT est présent
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/api/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Récupère le header Authorization
        final String authHeader = request.getHeader("Authorization");

        // Si pas de header ou pas de Bearer token, on laisse passer la requête
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // On enlève "Bearer " pour garder seulement le token
        final String jwt = authHeader.substring(7);

        // On récupère l'email/username contenu dans le token
        final String userEmail = jwtService.extractUsername(jwt);

        // Si un utilisateur est trouvé et qu'aucune auth n'existe encore
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Charge l'utilisateur depuis la base
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            // Vérifie que le token est toujours valide
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // Crée l'objet d'authentification Spring Security
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Ajoute quelques détails sur la requête courante
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Stocke l'authentification dans le contexte Spring Security
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue le traitement normal de la requête
        filterChain.doFilter(request, response);
    }
}