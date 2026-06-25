package com.example.Gestion_des_evaluations.Entity.User.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String email;
    private String password;
    private String nom;
    private String prenom;
    private Set<Long> roleIds;


}