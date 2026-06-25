package com.example.Gestion_des_evaluations.Entity.Programmation.Mapper;

import com.example.Gestion_des_evaluations.Entity.Programmation.DTO.ProgrammationResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Programmation.Model.Programmation;

public class ProgrammationMapper {

    public static ProgrammationResponseDTO toDTO(Programmation p) {
        return new ProgrammationResponseDTO(
                p.getId(),
                p.getDateExamen(),
                p.getHeureDebut(),
                p.getHeureFin(),
                p.getSalle(),
                p.getStatut(),
                p.getEvaluation() != null ? p.getEvaluation().getId() : null
        );
    }
}