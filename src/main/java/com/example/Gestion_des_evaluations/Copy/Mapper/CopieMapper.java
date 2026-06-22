package com.example.Gestion_des_evaluations.Copy.Mapper;

import com.example.Gestion_des_evaluations.Copy.DTO.CopieResponseDTO;
import com.example.Gestion_des_evaluations.Copy.Model.Copie;

public class CopieMapper {

    public static CopieResponseDTO toDTO(Copie copie) {
        CopieResponseDTO dto = new CopieResponseDTO();
        dto.setId(copie.getId());
        dto.setFichierPath(copie.getFichierPath());
        dto.setDateDepot(copie.getDateDepot());
        dto.setDateRetour(copie.getDateRetour());
        dto.setDateRetrait(copie.getDateRetrait());
        dto.setNbreTotalCopie(copie.getNbreTotalCopie());
        dto.setStatut(copie.getStatut());
        dto.setSujetExamenId(copie.getSujetExamen() != null ? copie.getSujetExamen().getId() : null);
        dto.setEtudiantId(copie.getEtudiant() != null ? copie.getEtudiant().getId() : null);
        return dto;
    }
}