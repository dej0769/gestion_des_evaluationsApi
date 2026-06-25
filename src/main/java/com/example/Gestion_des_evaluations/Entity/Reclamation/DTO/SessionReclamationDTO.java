package com.example.Gestion_des_evaluations.Entity.Reclamation.DTO;

import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.SessionStatut;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionReclamationDTO {
    private Long id;
    private SessionStatut statut;
}