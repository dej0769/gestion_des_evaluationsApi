package com.example.Gestion_des_evaluations.Entity.Copie.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CopieRequestDTO {
    private Long sujetExamenId;
    private Long etudiantId;
}