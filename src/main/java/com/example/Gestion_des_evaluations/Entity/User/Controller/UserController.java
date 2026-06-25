package com.example.Gestion_des_evaluations.Entity.User.Controller;

import com.example.Gestion_des_evaluations.Entity.User.DTO.UserResponseDTO;
import com.example.Gestion_des_evaluations.Entity.User.Model.User;
import com.example.Gestion_des_evaluations.Entity.User.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base-url}/admin/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

//rechercher un user par son mail
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<UserResponseDTO> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }
//creer un utilisateur
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    //modifier un user
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    //desactiver un compte utilisateur
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }
    //reactiver un compte utilisateur
    @PutMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivateUser(@PathVariable Long id) {
        userService.reactivateUser(id);
        return ResponseEntity.noContent().build();
    }


    // Retourne la liste complète des utilisateurs
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")//verifie si l'utilisateur connecte est un administrateur
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    // Retourne un utilisateur précis par son id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
// Attribuer un role à un utilisateur
    @PostMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        return ResponseEntity.ok(userService.assignRoleToUser(userId, roleId));
    }
// Retirer un role d'un utilisateur
    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        return ResponseEntity.ok(userService.removeRoleFromUser(userId, roleId));
    }



    @GetMapping("/nom/{nom}")
    public List<UserResponseDTO> getByNom(@PathVariable String nom) {
        return userService.getByNom(nom);
    }

    @GetMapping("/prenom/{prenom}")
    public List<UserResponseDTO> getByPrenom(@PathVariable String prenom) {
        return userService.getByPrenom(prenom);
    }
}