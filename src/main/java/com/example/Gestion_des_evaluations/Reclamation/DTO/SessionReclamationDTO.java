package com.example.Gestion_des_evaluations.Reclamation.DTO;

import com.example.Gestion_des_evaluations.Reclamation.Model.SessionStatut;
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