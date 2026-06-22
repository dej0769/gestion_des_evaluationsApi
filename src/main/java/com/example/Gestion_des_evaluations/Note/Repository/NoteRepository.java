package com.example.Gestion_des_evaluations.Note.Repository;

import com.example.Gestion_des_evaluations.Note.Model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByCopieId(Long copieId);

    List<Note> findByCorrecteurId(Long correcteurId);

    List<Note> findByCopieEtudiantId(Long etudiantId);
}