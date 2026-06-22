package com.example.Gestion_des_evaluations.Incident.Repository;

import com.example.Gestion_des_evaluations.Incident.Model.Incident;
import com.example.Gestion_des_evaluations.Incident.Model.IncidentStatut;
import com.example.Gestion_des_evaluations.Incident.Model.IncidentType;
import com.example.Gestion_des_evaluations.Reclamation.Model.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByEvaluationId(Long evaluationId);

    List<Incident> findByStatut(IncidentStatut statut);

    List<Incident> findByType(IncidentType type);

    List<Incident> findByDateSignalementBetween(LocalDateTime debut, LocalDateTime fin);
}