package com.example.Gestion_des_evaluations.Auth.Repository;

import com.example.Gestion_des_evaluations.Auth.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}