package com.example.Gestion_des_evaluations.Copy.DTO;


import com.example.Gestion_des_evaluations.Copy.Model.StatutCopie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CopieResponseDTO {
    private Long id;
    private String fichierPath;
    private LocalDateTime dateDepot;
    private LocalDateTime dateRetour;
    private LocalDateTime dateRetrait;
    private int nbreTotalCopie;
    private StatutCopie statut;
    private Long sujetExamenId;
    private Long etudiantId;
}