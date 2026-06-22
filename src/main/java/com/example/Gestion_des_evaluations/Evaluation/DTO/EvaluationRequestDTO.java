package com.example.Gestion_des_evaluations.Evaluation.DTO;

import com.example.Gestion_des_evaluations.Evaluation.Model.Evaluation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationRequestDTO {

    private LocalDate dateEvaluation;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String salle;
    private String module;
    private String promotion;
    private String filiere;
    private String niveau;
    private String semestre;
    private String session;

}
