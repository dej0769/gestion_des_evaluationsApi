package com.example.Gestion_des_evaluations.Entity.Reclamation.Repository;

import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.Reclamation;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.ReclamationStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {

    List<Reclamation> findByNoteId(Long noteId);

    List<Reclamation> findByStatut(ReclamationStatut statut);

    List<Reclamation> findByDateCreation(LocalDate dateCreation);

    @Query("""
        select r
        from Reclamation r
        join r.note n
        join n.copie c
        join c.sujetExamen s
        join s.evaluation e
        join Programmation p on p.evaluation = e
        where p.id = :programmationId
    """)
    List<Reclamation> findByProgrammationId(@Param("programmationId") Long programmationId);

    List<Reclamation> findBySessionReclamationId(Long sessionId);
}