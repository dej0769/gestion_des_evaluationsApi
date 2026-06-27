package com.example.Gestion_des_evaluations.Entity.Auth.Service;

import com.example.Gestion_des_evaluations.Entity.Action.Model.TypeAction;
import com.example.Gestion_des_evaluations.Entity.Action.Service.ActionService;
import com.example.Gestion_des_evaluations.Entity.Auth.DTO.*;
import com.example.Gestion_des_evaluations.Entity.Auth.Model.LoginOtp;
import com.example.Gestion_des_evaluations.Entity.Auth.Repository.LoginOtpRepository;
import com.example.Gestion_des_evaluations.Entity.Sujet.Event.AuditActionEvent;
import com.example.Gestion_des_evaluations.Entity.User.DTO.UserResponseDTO;
import com.example.Gestion_des_evaluations.Entity.User.Model.PasswordResetToken;
import com.example.Gestion_des_evaluations.Entity.User.Model.Role;
import com.example.Gestion_des_evaluations.Entity.User.Model.User;
import com.example.Gestion_des_evaluations.Entity.User.Repository.PasswordResetTokenRepository;
import com.example.Gestion_des_evaluations.Entity.User.Repository.UserRepository;
import com.example.Gestion_des_evaluations.Entity.Auth.Security.CustomUserDetailsService;
import com.example.Gestion_des_evaluations.Entity.Auth.Security.JwtService;
import com.example.Gestion_des_evaluations.Service.Mail.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// Contient la logique métier de connexion
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final ActionService actionService;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final LoginOtpRepository otpRepository;

    @Value("${app.frontend.reset-password-url:http://localhost:3000/reset-password}")
    private String resetPasswordUrl;

    @Transactional
    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe invalide");
        }

        otpRepository.deleteByEmailAndUsedFalse(user.getEmail());

        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

        LoginOtp loginOtp = new LoginOtp();
        loginOtp.setEmail(user.getEmail());
        loginOtp.setCode(otp);
        loginOtp.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        loginOtp.setUsed(false);

        otpRepository.save(loginOtp);
        emailService.sendOtpEmail(user.getEmail(), otp);

        return LoginResponseDTO.builder()
                .otpRequired(true)
                .message("OTP envoyé par email")
                .build();
    }

    @Transactional
    public LoginResponseDTO verifyOtp(OtpVerifyRequestDTO request) {
        LoginOtp otp = otpRepository.findTopByEmailAndUsedFalseOrderByIdDesc(request.getEmail())
                .orElseThrow(() -> new RuntimeException("OTP introuvable"));

        if (otp.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expiré");
        }

        if (!otp.getCode().equals(request.getOtp())) {
            throw new RuntimeException("OTP invalide");
        }

        otp.setUsed(true);
        otpRepository.save(otp);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
        String jwt = jwtService.generateToken(userDetails);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        UserResponseDTO userDTO = new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getNom(),
                user.getPrenom(),
                roles
        );

        return LoginResponseDTO.builder()
                .token(jwt)
                .otpRequired(false)
                .message("Connexion réussie")
                .user(userDTO)
                .build();
    }

    @Transactional
    public String forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        tokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        tokenRepository.save(resetToken);

        String link = resetPasswordUrl + "?token=" + token;
        emailService.sendResetPasswordEmail(user.getEmail(), link);

        return "Lien de réinitialisation envoyé";
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Token invalide"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expiré");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        tokenRepository.delete(resetToken);
    }

}
