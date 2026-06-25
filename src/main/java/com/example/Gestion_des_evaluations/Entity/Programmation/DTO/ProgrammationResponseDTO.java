package com.example.Gestion_des_evaluations.Entity.Programmation.DTO;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgrammationResponseDTO {
    private Long id;
    private LocalDate dateExamen;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String salle;
    private String statut;
    private Long evaluationId;
}