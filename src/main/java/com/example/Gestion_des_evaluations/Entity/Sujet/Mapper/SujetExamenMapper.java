package com.example.Gestion_des_evaluations.Entity.Sujet.Mapper;


import com.example.Gestion_des_evaluations.Entity.Sujet.DTO.SujetExamenRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Sujet.DTO.SujetExamenResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Sujet.Model.SujetExamen;

//Conversion du DTO de création vers l'entité JPA
public class SujetExamenMapper {

    public static SujetExamen toEntity(SujetExamenRequestDTO dto) {

        if (dto == null) {
            return null;
        }
        SujetExamen sujet = new SujetExamen();
        sujet.setTitre(dto.getTitre());
        sujet.setDescription(dto.getDescription());
        sujet.setModule(dto.getModule());
        sujet.setPromotion(dto.getPromotion());
        sujet.setFiliere(dto.getFiliere());
        sujet.setNiveau(dto.getNiveau());
        sujet.setSemestre(dto.getSemestre());
        return sujet;
    }
//conversion pour la modification
    public static void updateEntity(SujetExamen sujet, SujetExamenRequestDTO dto) {

        sujet.setTitre(dto.getTitre());
        sujet.setDescription(dto.getDescription());
        sujet.setModule(dto.getModule());
        sujet.setPromotion(dto.getPromotion());
        sujet.setFiliere(dto.getFiliere());
        sujet.setNiveau(dto.getNiveau());
        sujet.setSemestre(dto.getSemestre());
    }


    // Conversion de l'entité vers un DTO de réponse.
    public static SujetExamenResponseDTO toDTO(SujetExamen sujet) {
        if  (sujet == null) {
            return null;
        }
        SujetExamenResponseDTO dto = new SujetExamenResponseDTO();
        dto.setId(sujet.getId());
        dto.setTitre(sujet.getTitre());
        dto.setDescription(sujet.getDescription());
        dto.setModule(sujet.getModule());
        dto.setPromotion(sujet.getPromotion());
        dto.setFiliere(sujet.getFiliere());
        dto.setNiveau(sujet.getNiveau());
        dto.setSemestre(sujet.getSemestre());
        dto.setDateCreation(sujet.getDateCreation());
        dto.setStatut(sujet.getStatut());
        return dto;
    }
}