package com.example.Gestion_des_evaluations.Auth.Controller;

import com.example.Gestion_des_evaluations.Auth.DTO.VerifyOtpRequestDTO;
import com.example.Gestion_des_evaluations.Auth.Service.TwoFactorAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/2fa")
@RequiredArgsConstructor
public class TwoFactorAuthController {

    private final TwoFactorAuthService twoFactorAuthService;

    @PostMapping("/activate")
    public ResponseEntity<Map<String, String>> activate(@RequestParam String email) throws Exception {
        String qrCodeBase64 = twoFactorAuthService.activate2FA(email);
        return ResponseEntity.ok(Map.of("qrCode", qrCodeBase64));
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Boolean>> verify(@RequestBody VerifyOtpRequestDTO request) {
        boolean valid = twoFactorAuthService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(Map.of("valid", valid));
    }
}
