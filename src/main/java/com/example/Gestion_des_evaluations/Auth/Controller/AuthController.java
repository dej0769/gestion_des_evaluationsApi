package com.example.Gestion_des_evaluations.Auth.Controller;

import com.example.Gestion_des_evaluations.Auth.DTO.LoginRequestDTO;
import com.example.Gestion_des_evaluations.Auth.DTO.LoginResponseDTO;
import com.example.Gestion_des_evaluations.Auth.DTO.VerifyOtpRequestDTO;
import com.example.Gestion_des_evaluations.Auth.Service.AuthService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<LoginResponseDTO> verifyOtp(@Valid @RequestBody VerifyOtpRequestDTO request) {
        return ResponseEntity.ok(authService.verifyOtp(request.getEmail(), request.getCode()));
    }

    @GetMapping(value = "/enable-2fa/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQrCode(@RequestParam String email) throws Exception {
        String otpAuthUrl = authService.enable2FA(email);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthUrl, BarcodeFormat.QR_CODE, 250, 250);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(outputStream.toByteArray());
    }
}