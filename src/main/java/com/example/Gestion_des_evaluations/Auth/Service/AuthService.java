package com.example.Gestion_des_evaluations.Auth.Service;

import com.example.Gestion_des_evaluations.Action.Model.TypeAction;
import com.example.Gestion_des_evaluations.Action.Service.ActionService;
import com.example.Gestion_des_evaluations.Auth.DTO.LoginRequestDTO;
import com.example.Gestion_des_evaluations.Auth.DTO.LoginResponseDTO;
import com.example.Gestion_des_evaluations.Subject.Event.AuditActionEvent;
import com.example.Gestion_des_evaluations.User.Model.Role;
import com.example.Gestion_des_evaluations.User.Model.User;
import com.example.Gestion_des_evaluations.User.Repository.RoleRepository;
import com.example.Gestion_des_evaluations.User.Repository.UserRepository;
import com.example.Gestion_des_evaluations.Auth.Security.CustomUserDetailsService;
import com.example.Gestion_des_evaluations.Auth.Security.JwtService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

// Contient la logique métier de connexion
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ActionService actionService;
    private final TwoFactorAuthService twoFactorAuthService;

    public AuthService(AuthenticationManager authenticationManager,
                       CustomUserDetailsService customUserDetailsService,
                       JwtService jwtService,
                       UserRepository userRepository,
                       ActionService actionService,
                       TwoFactorAuthService twoFactorAuthService) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.actionService = actionService;
        this.twoFactorAuthService = twoFactorAuthService;
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        if (user.isTwoFactorEnabled()) {
            return new LoginResponseDTO(
                    null,
                    user.getEmail(),
                    user.getNom(),
                    user.getPrenom(),
                    roles,
                    true
            );
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(dto.getEmail());
        String token = jwtService.generateToken(userDetails);

        actionService.enregistrerAutomatiquement(new AuditActionEvent(
                user.getId(),
                TypeAction.CONNEXION,
                "Connexion réussie de " + user.getEmail()
        ));

        return new LoginResponseDTO(
                token,
                user.getEmail(),
                user.getNom(),
                user.getPrenom(),
                roles,
                false
        );
    }

    public String enable2FA(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();

        user.setSecret2FA(key.getKey());
        user.setTwoFactorEnabled(true);
        userRepository.save(user);

        return GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(
                "GestionEvaluations",
                user.getEmail(),
                key
        );
    }

    public LoginResponseDTO verifyOtp(String email, int code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!user.isTwoFactorEnabled()) {
            throw new RuntimeException("2FA non activée");
        }

        boolean valid = twoFactorAuthService.verifyCode(email, code);
        if (!valid) {
            throw new RuntimeException("Code OTP invalide");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        String token = jwtService.generateToken(userDetails);

        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        actionService.enregistrerAutomatiquement(new AuditActionEvent(
                user.getId(),
                TypeAction.CONNEXION,
                "Connexion réussie avec 2FA de " + user.getEmail()
        ));

        return new LoginResponseDTO(
                token,
                user.getEmail(),
                user.getNom(),
                user.getPrenom(),
                roles,
                false
        );
    }
}