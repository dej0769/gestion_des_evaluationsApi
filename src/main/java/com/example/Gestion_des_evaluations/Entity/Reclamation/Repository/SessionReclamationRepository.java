package com.example.Gestion_des_evaluations.Entity.Reclamation.Repository;

import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.SessionReclamation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionReclamationRepository extends JpaRepository<SessionReclamation, Long> {
}