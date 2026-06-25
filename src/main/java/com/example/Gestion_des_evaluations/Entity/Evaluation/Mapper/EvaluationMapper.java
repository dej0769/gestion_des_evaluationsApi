package com.example.Gestion_des_evaluations.Entity.Evaluation.Mapper;

import com.example.Gestion_des_evaluations.Entity.Evaluation.DTO.EvaluationRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Evaluation.DTO.EvaluationResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Model.Evaluation;

public class EvaluationMapper {

    // Conversion du DTO de création vers l'entité JPA.
    public static Evaluation toEntity(EvaluationRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Evaluation evaluation = new Evaluation();
        evaluation.setDateEvaluation(dto.getDateEvaluation());
        evaluation.setHeureDebut(dto.getHeureDebut());
        evaluation.setHeureFin(dto.getHeureFin());
        evaluation.setSalle(dto.getSalle());
        evaluation.setModule(dto.getModule());
        evaluation.setModule(dto.getModule());
        evaluation.setPromotion(dto.getPromotion());
        evaluation.setFiliere(dto.getFiliere());
        evaluation.setSession(dto.getSession());
        evaluation.setNiveau(dto.getNiveau());
        evaluation.setSemestre(dto.getSemestre());
        return evaluation;
    }
    // Conversion de l'entité vers un DTO de réponse.
    public static EvaluationResponseDTO toDTO(Evaluation evaluation) {

        if (evaluation == null) {
            return null;
        }
        EvaluationResponseDTO dto = new EvaluationResponseDTO();
        dto.setId(evaluation.getId());
        dto.setDateEvaluation(evaluation.getDateEvaluation());
        dto.setHeureDebut(evaluation.getHeureDebut());
        dto.setHeureFin(evaluation.getHeureFin());
        dto.setSalle(evaluation.getSalle());
        dto.setModule(evaluation.getModule());
        dto.setPromotion(evaluation.getPromotion());
        dto.setFiliere(evaluation.getFiliere());
        dto.setSession(evaluation.getSession());
        dto.setNiveau(evaluation.getNiveau());
        dto.setSemestre(evaluation.getSemestre());
        dto.setStatut(evaluation.getStatut());
        return dto;
    }
    // Mise à jour contrôlée d'une évaluation existante
    public static void updateEntity(Evaluation evaluation, EvaluationRequestDTO dto) {
        evaluation.setDateEvaluation(dto.getDateEvaluation());
        evaluation.setHeureDebut(dto.getHeureDebut());
        evaluation.setHeureFin(dto.getHeureFin());
        evaluation.setSalle(dto.getSalle());
        evaluation.setModule(dto.getModule());
        evaluation.setPromotion(dto.getPromotion());
        evaluation.setFiliere(dto.getFiliere());
        evaluation.setSession(dto.getSession());
        evaluation.setNiveau(dto.getNiveau());
        evaluation.setSemestre(dto.getSemestre());
    }
}