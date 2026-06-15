package com.example.Gestion_des_evaluations.Auth.Service;

import com.example.Gestion_des_evaluations.Auth.DTO.LoginRequestDTO;
import com.example.Gestion_des_evaluations.Auth.DTO.LoginResponseDTO;
import com.example.Gestion_des_evaluations.Auth.DTO.RegisterRequestDTO;
import com.example.Gestion_des_evaluations.Auth.Model.Role;
import com.example.Gestion_des_evaluations.Auth.Model.User;
import com.example.Gestion_des_evaluations.Auth.Repository.RoleRepository;
import com.example.Gestion_des_evaluations.Auth.Repository.UserRepository;
import com.example.Gestion_des_evaluations.Auth.Security.CustomUserDetailsService;
import com.example.Gestion_des_evaluations.Auth.Security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

// Contient la logique métier de connexion
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthService(AuthenticationManager authenticationManager,
                       CustomUserDetailsService customUserDetailsService,
                       JwtService jwtService, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Authentifie l'utilisateur et génère le token JWT
    public LoginResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(dto.getEmail());
        String token = jwtService.generateToken(userDetails);

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new LoginResponseDTO(
                token,
                user.getEmail(),
                user.getNom(),
                user.getPrenom(),
                roles
        );
    }
    public User register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        Role studentRole = roleRepository.findByName("ETUDIANT")
                .orElseThrow(() -> new RuntimeException("Rôle ETUDIANT introuvable"));

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());

        user.setRoles(Set.of(studentRole));

        return userRepository.save(user);
    }

}
