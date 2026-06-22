package com.example.Gestion_des_evaluations.Auth.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String email;
    private String nom;
    private String prenom;
    private Set<String> roles;
    private boolean otpRequired;
}