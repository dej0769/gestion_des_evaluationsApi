package com.example.Gestion_des_evaluations.Reclamation.Repository;

import com.example.Gestion_des_evaluations.Reclamation.Model.SessionReclamation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionReclamationRepository extends JpaRepository<SessionReclamation, Long> {
}