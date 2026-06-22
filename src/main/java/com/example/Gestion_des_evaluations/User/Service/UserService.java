package com.example.Gestion_des_evaluations.User.Service;


import com.example.Gestion_des_evaluations.Action.Model.TypeAction;
import com.example.Gestion_des_evaluations.Action.Service.ActionService;
import com.example.Gestion_des_evaluations.Subject.Event.AuditActionEvent;
import com.example.Gestion_des_evaluations.User.DTO.UserResponseDTO;
import com.example.Gestion_des_evaluations.User.Mapper.UserMapper;
import com.example.Gestion_des_evaluations.User.Model.Role;
import com.example.Gestion_des_evaluations.User.Model.User;
import com.example.Gestion_des_evaluations.User.Repository.RoleRepository;
import com.example.Gestion_des_evaluations.User.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;
    private final ActionService actionService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, CurrentUserService currentUserService, ActionService actionService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.currentUserService = currentUserService;
        this.actionService = actionService;
    }

//ajouter un utilisateur
public UserResponseDTO saveUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    Role studentRole = roleRepository.findByName("ETUDIANT")
            .orElseThrow(() -> new RuntimeException("Rôle ETUDIANT introuvable"));

    if (user.getRoles() == null) {
        user.setRoles(new HashSet<>());
    }

    user.getRoles().add(studentRole);

    User saved = userRepository.save(user);

    Long auteurId = currentUserService.getCurrentUserId();
    actionService.enregistrerAutomatiquement(new AuditActionEvent(
            auteurId,
            TypeAction.CREATION_UTILISATEUR,
            "Création de l'utilisateur " + saved.getEmail()
    ));

    return UserMapper.toDTO(saved);
}
// modifier un user
public UserResponseDTO updateUser(Long id, User updatedUser) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

    user.setEmail(updatedUser.getEmail());
    user.setNom(updatedUser.getNom());
    user.setPrenom(updatedUser.getPrenom());

    if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
    }

    User saved = userRepository.save(user);

    Long auteurId = currentUserService.getCurrentUserId();
    actionService.enregistrerAutomatiquement(new AuditActionEvent(
            auteurId,
            TypeAction.MODIFICATION_UTILISATEUR,
            "Modification de l'utilisateur " + saved.getEmail()
    ));

    return UserMapper.toDTO(saved);
}
//desactiver un compte user
public void deactivateUser(Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    user.setActive(false);
    userRepository.save(user);

    Long auteurId = currentUserService.getCurrentUserId();
    actionService.enregistrerAutomatiquement(new AuditActionEvent(
            auteurId,
            TypeAction.DESACTIVATION_COMPTE,
            "Désactivation du compte utilisateur " + user.getEmail()
    ));
}
//reactiver un compte
    public void reactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        user.setActive(true);
        userRepository.save(user);

        Long auteurId = currentUserService.getCurrentUserId();
        actionService.enregistrerAutomatiquement(new AuditActionEvent(
                auteurId,
                TypeAction.REACTIVATION_COMPTE,
                "Réactivation du compte utilisateur " + user.getEmail()
        ));
    }

//assigner un role a un user
public UserResponseDTO assignRoleToUser(Long userId, Long roleId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

    Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Rôle introuvable"));

    user.getRoles().add(role);
    User saved = userRepository.save(user);

    Long auteurId = currentUserService.getCurrentUserId();
    actionService.enregistrerAutomatiquement(new AuditActionEvent(
            auteurId,
            TypeAction.AFFECTATION_ROLE,
            "Affectation du rôle " + role.getName() + " à " + saved.getEmail()
    ));

    return UserMapper.toDTO(saved);
}
//revoquer un role a un utilisateur
public UserResponseDTO removeRoleFromUser(Long userId, Long roleId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

    user.getRoles().removeIf(r -> r.getId().equals(roleId));
    User saved = userRepository.save(user);

    Long auteurId = currentUserService.getCurrentUserId();
    actionService.enregistrerAutomatiquement(new AuditActionEvent(
            auteurId,
            TypeAction.RETRAIT_ROLE,
            "Retrait d'un rôle à l'utilisateur " + saved.getEmail()
    ));

    return UserMapper.toDTO(saved);
}

    // Retourne un utilisateur par son email
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return UserMapper.toDTO(user);
    }

    // Récupère tous les utilisateurs enregistrés en base
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }
    // Retourne un utilisateur précis par son identifiant
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return UserMapper.toDTO(user);
    }

    public List<UserResponseDTO> getByNom(String nom) {
        return userRepository.findByNomContainingIgnoreCase(nom)
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public List<UserResponseDTO> getByPrenom(String prenom) {
        return userRepository.findByPrenomContainingIgnoreCase(prenom)
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }
}
