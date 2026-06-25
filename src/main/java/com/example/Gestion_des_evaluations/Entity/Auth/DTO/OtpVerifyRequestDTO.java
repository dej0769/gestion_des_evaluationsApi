package com.example.Gestion_des_evaluations.Entity.Auth.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OtpVerifyRequestDTO {
    private String email;
    private String otp;
}