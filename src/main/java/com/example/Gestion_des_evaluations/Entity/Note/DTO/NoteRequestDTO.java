package com.example.Gestion_des_evaluations.Entity.Note.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteRequestDTO {
    private Double valeur;
    private String commentaire;
    private Integer nbreTotalEtudiantsComposes;
    private Double nbreEtudiantsAyantLaMoyenne;
    private Long copieId;
    private Long correcteurId;
}
