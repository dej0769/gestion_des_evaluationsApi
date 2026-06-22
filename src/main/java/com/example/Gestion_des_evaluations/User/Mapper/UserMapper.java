package com.example.Gestion_des_evaluations.User.Mapper;


import com.example.Gestion_des_evaluations.User.DTO.UserResponseDTO;
import com.example.Gestion_des_evaluations.User.Model.Role;
import com.example.Gestion_des_evaluations.User.Model.User;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {


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
