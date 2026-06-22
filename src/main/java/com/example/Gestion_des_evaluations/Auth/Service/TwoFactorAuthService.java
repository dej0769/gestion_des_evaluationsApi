package com.example.Gestion_des_evaluations.Auth.Service;

import com.example.Gestion_des_evaluations.User.Model.User;
import com.example.Gestion_des_evaluations.User.Repository.UserRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.util.Base64;


@Service
@RequiredArgsConstructor
public class TwoFactorAuthService {

    private final UserRepository userRepository;
    private final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

    public String activate2FA(String email) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        String secret = key.getKey();

        user.setSecret2FA(secret);
        user.setTwoFactorEnabled(true);
        userRepository.save(user);

        return generateQrCode(email, secret, "GestionEvaluations");
    }

    public boolean verifyCode(String email, int code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!user.isTwoFactorEnabled() || user.getSecret2FA() == null) {
            throw new RuntimeException("2FA non activée");
        }

        return googleAuthenticator.authorize(user.getSecret2FA(), code);
    }

    private String generateQrCode(String email, String secret, String issuer) throws Exception {
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(issuer, email, key);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 250, 250);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
}