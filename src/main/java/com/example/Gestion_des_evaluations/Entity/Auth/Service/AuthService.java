package com.example.Gestion_des_evaluations.Entity.Auth.Service;

import com.example.Gestion_des_evaluations.Entity.Action.Model.TypeAction;
import com.example.Gestion_des_evaluations.Entity.Action.Service.ActionService;
import com.example.Gestion_des_evaluations.Entity.Auth.DTO.LoginRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Auth.DTO.LoginResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Auth.DTO.OtpVerifyRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Sujet.Event.AuditActionEvent;
import com.example.Gestion_des_evaluations.Entity.User.Model.User;
import com.example.Gestion_des_evaluations.Entity.User.Repository.UserRepository;
import com.example.Gestion_des_evaluations.Entity.Auth.Security.CustomUserDetailsService;
import com.example.Gestion_des_evaluations.Entity.Auth.Security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

// Contient la logique métier de connexion
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpSenderService otpSenderService;
    private final CustomUserDetailsService customUserDetailsService;
    private final ActionService actionService;

    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();

    @Value("${otp.expiration-minutes}")
    private long otpExpirationMinutes;

    public LoginResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        String otp = generateOtp();
        otpStore.put(user.getEmail(), new OtpData(otp, LocalDateTime.now().plusMinutes(otpExpirationMinutes)));

        otpSenderService.sendOtp(user.getTelephone(), "Votre code OTP est : " + otp);

        return LoginResponseDTO.builder()
                .token(null)
                .otpRequired(true)
                .message("Code OTP envoyé par SMS")
                .build();
    }

    public LoginResponseDTO verifyOtp(OtpVerifyRequestDTO dto) {
        OtpData otpData = otpStore.get(dto.getEmail());

        if (otpData == null) {
            throw new RuntimeException("Aucun OTP trouvé");
        }

        if (otpData.expiry.isBefore(LocalDateTime.now())) {
            otpStore.remove(dto.getEmail());
            throw new RuntimeException("OTP expiré");
        }

        if (!otpData.code.equals(dto.getOtp())) {
            throw new RuntimeException("OTP invalide");
        }

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        otpStore.remove(dto.getEmail());

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);

        actionService.enregistrerAutomatiquement(new AuditActionEvent(
                user.getId(),
                TypeAction.CONNEXION,
                "Connexion réussie avec OTP de " + user.getEmail()
        ));

        return LoginResponseDTO.builder()
                .token(token)
                .otpRequired(false)
                .message("Connexion réussie")
                .build();
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private static class OtpData {
        private final String code;
        private final LocalDateTime expiry;

        private OtpData(String code, LocalDateTime expiry) {
            this.code = code;
            this.expiry = expiry;
        }
    }
}