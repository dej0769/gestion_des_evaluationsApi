package com.example.Gestion_des_evaluations.Entity.Sujet.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SujetExamenActionResponseDTO {
    private Long id;
    private String titre;
    private String statut;
    private LocalDate dateCreation;
    private String module;
    private Long enseignantId;
    private Long evaluationId;
    private String message;
}
