package com.example.Gestion_des_evaluations.Entity.User.Repository;

import com.example.Gestion_des_evaluations.Entity.User.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Cherche un utilisateur à partir de son email
    Optional<User> findByEmail(String email);

    // Vérifie si un email existe déjà en base
    boolean existsByEmail(String email);

    List<User> findByNomContainingIgnoreCase(String nom);

    List<User> findByPrenomContainingIgnoreCase(String prenom);
}
