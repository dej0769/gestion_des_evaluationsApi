package com.example.Gestion_des_evaluations.Subject.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SujetExamenRequestDTO {
    private String titre;
    private String description;
    private String module;
    private String promotion;
    private String filiere;
    private String niveau;
    private String semestre;
    private LocalDate dateCreation;
    private Long enseignantId;
}

