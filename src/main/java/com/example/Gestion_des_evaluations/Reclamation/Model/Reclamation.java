package com.example.Gestion_des_evaluations.Reclamation.Model;

import com.example.Gestion_des_evaluations.Note.Model.Note;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reclamations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateCreation;
    private String description;
    private String fichier;
    private String objet;
    private String typeDocument;

    @Enumerated(EnumType.STRING)
    private ReclamationStatut statut;

    @ManyToOne
    private SessionReclamation sessionReclamation;

    @ManyToOne
    @JoinColumn(name = "note_id")
    private Note note;
}