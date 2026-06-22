package com.example.Gestion_des_evaluations.Subject.DTO;

import com.example.Gestion_des_evaluations.Subject.Model.StatutSujetExamen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SujetExamenResponseDTO {
    private Long id;
    private String titre;
    private String description;
    private String module;
    private String promotion;
    private String filiere;
    private String niveau;
    private String semestre;
    private LocalDate dateCreation;
    private StatutSujetExamen statut;
}