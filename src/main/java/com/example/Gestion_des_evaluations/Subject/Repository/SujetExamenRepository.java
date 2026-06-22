package com.example.Gestion_des_evaluations.Subject.Repository;

import com.example.Gestion_des_evaluations.Subject.Model.StatutSujetExamen;
import com.example.Gestion_des_evaluations.Subject.Model.SujetExamen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SujetExamenRepository extends JpaRepository<SujetExamen, Long> {
    List<SujetExamen> findByStatut(StatutSujetExamen statut);
    List<SujetExamen> findByModule(String module);
    List<SujetExamen> findByEnseignantId(Long enseignantId);
    List<SujetExamen> findByEvaluationId(Long evaluationId);
}