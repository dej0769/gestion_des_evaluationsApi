package com.example.Gestion_des_evaluations.Entity.Auth.DTO;

import com.example.Gestion_des_evaluations.Entity.User.DTO.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {
    private String token;
    private boolean otpRequired;
    private String message;
    private UserResponseDTO user;
}