package com.example.Gestion_des_evaluations.Entity.Auth.Controller;

import com.example.Gestion_des_evaluations.Entity.Auth.DTO.LoginRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Auth.DTO.LoginResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Auth.DTO.OtpVerifyRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Auth.Service.AuthService;
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
@RequestMapping("${app.base-url}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<LoginResponseDTO> verifyOtp(@RequestBody OtpVerifyRequestDTO dto) {
        return ResponseEntity.ok(authService.verifyOtp(dto));
    }
}