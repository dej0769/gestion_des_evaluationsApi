package com.example.Gestion_des_evaluations.Entity.Note.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponseDTO {
    private Long id;
    private Double valeur;
    private String commentaire;
    private LocalDateTime dateCorrection;
    private Integer nbreTotalEtudiantsComposes;
    private Double nbreEtudiantsAyantLaMoyenne;
    private Long copieId;
    private Long correcteurId;
}
