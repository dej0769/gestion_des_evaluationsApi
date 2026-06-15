package com.example.Gestion_des_evaluations.Auth.Controller;

import com.example.Gestion_des_evaluations.Auth.DTO.LoginRequestDTO;
import com.example.Gestion_des_evaluations.Auth.DTO.LoginResponseDTO;
import com.example.Gestion_des_evaluations.Auth.DTO.RegisterRequestDTO;
import com.example.Gestion_des_evaluations.Auth.DTO.UserResponseDTO;
import com.example.Gestion_des_evaluations.Auth.Model.Role;
import com.example.Gestion_des_evaluations.Auth.Model.User;
import com.example.Gestion_des_evaluations.Auth.Service.AuthService;
import com.example.Gestion_des_evaluations.Auth.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

// Expose les routes d'authentification
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Inscription d'un nouvel utilisateur
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequestDTO dto) {
        User user = authService.register(dto);

        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        UserResponseDTO response = new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getNom(),
                user.getPrenom(),
                roles

        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // Connexion et génération du JWT
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}