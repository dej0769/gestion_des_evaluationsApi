package com.example.Gestion_des_evaluations.Auth.Mapper;


import com.example.Gestion_des_evaluations.Auth.DTO.RegisterRequestDTO;
import com.example.Gestion_des_evaluations.Auth.DTO.UserResponseDTO;
import com.example.Gestion_des_evaluations.Auth.Model.Role;
import com.example.Gestion_des_evaluations.Auth.Model.User;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    // Convertit le DTO d'inscription en entité User
    public static User toEntity(RegisterRequestDTO dto, Role defaultRole) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());

        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles); // rôle par défaut
        return user;
    }

    // Convertit l'entité User en réponse API sans exposer le mot de passe
    public static UserResponseDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getNom(),
                user.getPrenom(),
                roles
        );
    }
}
