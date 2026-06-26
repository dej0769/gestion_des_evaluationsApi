package com.example.Gestion_des_evaluations.Entity.Note.Repository;

import com.example.Gestion_des_evaluations.Entity.Note.Model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {


    List<Note> findByCopieId(Long copieId);

    Optional<Note> findByCopie_Correcteur_Id(Long correcteurId);

    List<Note> findByCopie_Id(Long copieId);

    List<Note> findByCopie_Etudiant_Id(Long etudiantId);
}