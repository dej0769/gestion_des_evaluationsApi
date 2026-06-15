package com.example.Gestion_des_evaluations.Auth.Repository;

import com.example.Gestion_des_evaluations.Auth.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Cherche un utilisateur à partir de son email
    Optional<User> findByEmail(String email);

    // Vérifie si un email existe déjà en base
    boolean existsByEmail(String email);
}
