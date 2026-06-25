package com.example.Gestion_des_evaluations.Entity.Reclamation.Mapper;

import com.example.Gestion_des_evaluations.Entity.Reclamation.DTO.SessionReclamationDTO;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.SessionReclamation;

public class SessionReclamationMapper {

    public static SessionReclamationDTO toDTO(SessionReclamation session) {
        return new SessionReclamationDTO(
                session.getId(),
                session.getStatut()
        );
    }
}