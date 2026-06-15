package com.example.Gestion_des_evaluations.Auth.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequestDTO {
    private String email;
    private String password;
    private String nom;
    private String prenom;
}
