package com.example.Gestion_des_evaluations.Entity.Auth.Repository;

import com.example.Gestion_des_evaluations.Entity.Auth.Model.LoginOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginOtpRepository extends JpaRepository<LoginOtp, Long> {
    Optional<LoginOtp> findTopByEmailAndUsedFalseOrderByIdDesc(String email);
    void deleteByEmailAndUsedFalse(String email);
}