package com.example.Gestion_des_evaluations.Entity.Programmation.Repository;

import com.example.Gestion_des_evaluations.Entity.Programmation.Model.Programmation;
import com.example.Gestion_des_evaluations.Entity.User.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProgrammationRepository extends JpaRepository<Programmation, Long> {
    List<Programmation> findByEvaluationId(Long evaluationId);

    List<Programmation> findBySalle(String salle);

    List<Programmation> findByDateExamen(LocalDate dateExamen);

}