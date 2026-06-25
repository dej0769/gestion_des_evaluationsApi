package com.example.Gestion_des_evaluations.Entity.Incident.DTO;

import com.example.Gestion_des_evaluations.Entity.Incident.Model.IncidentStatut;
import com.example.Gestion_des_evaluations.Entity.Incident.Model.IncidentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentResponseDTO {
    private Long id;
    private LocalDateTime dateSignalement;
    private String description;
    private IncidentType type;
    private IncidentStatut statut;
    private Long evaluationId;
}
