package com.example.Gestion_des_evaluations.Entity.User.Repository;

import com.example.Gestion_des_evaluations.Entity.User.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}