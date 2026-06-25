package com.example.Gestion_des_evaluations.Entity.Sujet.Repository;

import com.example.Gestion_des_evaluations.Entity.Sujet.Model.StatutSujetExamen;
import com.example.Gestion_des_evaluations.Entity.Sujet.Model.SujetExamen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SujetExamenRepository extends JpaRepository<SujetExamen, Long> {
    List<SujetExamen> findByStatut(StatutSujetExamen statut);
    List<SujetExamen> findByEnseignantId(Long enseignantId);
    List<SujetExamen> findByEvaluationId(Long evaluationId);
}