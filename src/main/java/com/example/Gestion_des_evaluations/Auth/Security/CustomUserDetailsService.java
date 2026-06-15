package com.example.Gestion_des_evaluations.Auth.Security;

import com.example.Gestion_des_evaluations.Auth.Model.User;
import com.example.Gestion_des_evaluations.Auth.Repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// Service utilisé par Spring Security pour charger un utilisateur depuis la base
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Spring appelle cette méthode pour retrouver l'utilisateur qui se connecte
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Recherche de l'utilisateur dans la base par email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable" + email));

        // Conversion de notre entité User en objet attendu par Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),                 // identifiant de connexion
                user.getPassword(),              // mot de passe déjà haché
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                        .toList()
        );

    }
}