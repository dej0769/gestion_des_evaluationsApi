package com.example.Gestion_des_evaluations.Entity.Incident.Mapper;

import com.example.Gestion_des_evaluations.Entity.Incident.DTO.IncidentResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Incident.Model.Incident;

public class IncidentMapper {

    public static IncidentResponseDTO toDTO(Incident incident) {

        if (incident == null) {
            return null;
        }
        return new IncidentResponseDTO(
                incident.getId(),
                incident.getDateSignalement(),
                incident.getDescription(),
                incident.getType(),
                incident.getStatut(),
                incident.getEvaluation() != null ? incident.getEvaluation().getId() : null
        );
    }
}
