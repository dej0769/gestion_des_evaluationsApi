package com.example.Gestion_des_evaluations.Entity.Reclamation.Mapper;

import com.example.Gestion_des_evaluations.Entity.Reclamation.DTO.ReclamationResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.Reclamation;

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