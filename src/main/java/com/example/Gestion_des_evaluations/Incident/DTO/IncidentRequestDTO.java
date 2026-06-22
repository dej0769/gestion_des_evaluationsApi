package com.example.Gestion_des_evaluations.Incident.DTO;

import com.example.Gestion_des_evaluations.Incident.Model.IncidentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentRequestDTO {
    private LocalDateTime dateSignalement;
    private String description;
    private IncidentType type;
    private Long evaluationId;
}