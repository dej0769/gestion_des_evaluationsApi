package com.example.Gestion_des_evaluations.Auth.Controller;

import com.example.Gestion_des_evaluations.Auth.DTO.UserResponseDTO;
import com.example.Gestion_des_evaluations.Auth.Mapper.UserMapper;
import com.example.Gestion_des_evaluations.Auth.Repository.UserRepository;
import com.example.Gestion_des_evaluations.Auth.Service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Retourne la liste complète des utilisateurs
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")//verifie si l'utilisateur connecte est un administrateur
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    // Retourne un utilisateur précis par son id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return UserMapper.toDTO(userService.getUserById(id));
    }
// Attribuer un role à un utilisateur
    @PostMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        return UserMapper.toDTO(userService.assignRoleToUser(userId, roleId));
    }
// Retirer un role d'un utilisateur
    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        return UserMapper.toDTO(userService.removeRoleFromUser(userId, roleId));
    }


}