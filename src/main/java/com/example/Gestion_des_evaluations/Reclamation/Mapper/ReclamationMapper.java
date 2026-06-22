package com.example.Gestion_des_evaluations.Reclamation.Mapper;

import com.example.Gestion_des_evaluations.Reclamation.DTO.ReclamationResponseDTO;
import com.example.Gestion_des_evaluations.Reclamation.Model.Reclamation;

public class ReclamationMapper {

    public static ReclamationResponseDTO toDTO(Reclamation r) {
        return new ReclamationResponseDTO(
                r.getId(),
                r.getDateCreation(),
                r.getDescription(),
                r.getFichier(),
                r.getObjet(),
                r.getTypeDocument(),
                r.getStatut(),
                r.getNote() != null ? r.getNote().getId() : null,
                r.getSessionReclamation() != null ? r.getSessionReclamation().getId() : null
        );
    }
}