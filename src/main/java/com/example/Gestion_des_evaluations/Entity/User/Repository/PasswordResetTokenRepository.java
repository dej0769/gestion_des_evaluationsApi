package com.example.Gestion_des_evaluations.Entity.User.Repository;

import com.example.Gestion_des_evaluations.Entity.User.Model.PasswordResetToken;
import com.example.Gestion_des_evaluations.Entity.User.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(User user);
}