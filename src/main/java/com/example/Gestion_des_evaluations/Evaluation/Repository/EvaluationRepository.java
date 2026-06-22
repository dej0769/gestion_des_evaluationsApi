package com.example.Gestion_des_evaluations.Evaluation.Repository;

import com.example.Gestion_des_evaluations.Evaluation.Model.Evaluation;
import com.example.Gestion_des_evaluations.Evaluation.Model.StatutEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {



    List<Evaluation> findBySalleAndDateEvaluation(String salle, LocalDate dateEvaluation);

    // Sert à détecter un chevauchement d'horaire dans la même salle.
    boolean existsBySalleAndDateEvaluationAndHeureDebutLessThanAndHeureFinGreaterThan(
            String salle,
            LocalDate dateEvaluation,
            LocalTime heureFin,
            LocalTime heureDebut
    );

    List<Evaluation> findByProgrammationId(Long programmationId);

    List<Evaluation> findByStatut(StatutEvaluation statut);

    List<Evaluation> findByEnseignantId(Long enseignantId);

    List<Evaluation> findByDateEvaluation(LocalDate dateEvaluation);
}
