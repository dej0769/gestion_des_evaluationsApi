package com.example.Gestion_des_evaluations.Entity.Sujet.Mapper;

import com.example.Gestion_des_evaluations.Entity.Sujet.DTO.SujetExamenActionResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Sujet.Model.SujetExamen;

public class SujetExamenActionMapper {

    public static SujetExamenActionResponseDTO toActionDTO(SujetExamen sujet, String message) {
        if (sujet == null) {
            return null;
        }
        SujetExamenActionResponseDTO dto = new SujetExamenActionResponseDTO();
        dto.setId(sujet.getId());
        dto.setTitre(sujet.getTitre());
        dto.setStatut(sujet.getStatut().name());
        dto.setDateCreation(sujet.getDateCreation());
        dto.setModule(sujet.getModule());
        dto.setEnseignantId(sujet.getEnseignant() != null ? sujet.getEnseignant().getId() : null);
        dto.setEvaluationId(sujet.getEvaluation() != null ? sujet.getEvaluation().getId() : null);
        dto.setMessage(message);
        return dto;
    }
}
