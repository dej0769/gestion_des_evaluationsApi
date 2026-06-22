package com.example.Gestion_des_evaluations.Reclamation.Mapper;

import com.example.Gestion_des_evaluations.Reclamation.DTO.SessionReclamationDTO;
import com.example.Gestion_des_evaluations.Reclamation.Model.SessionReclamation;

public class SessionReclamationMapper {

    public static SessionReclamationDTO toDTO(SessionReclamation session) {
        return new SessionReclamationDTO(
                session.getId(),
                session.getStatut()
        );
    }
}