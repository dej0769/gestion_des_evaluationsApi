package com.example.Gestion_des_evaluations.Entity.Evaluation.Service;

import com.example.Gestion_des_evaluations.Entity.Action.Model.TypeAction;
import com.example.Gestion_des_evaluations.Entity.Evaluation.DTO.EvaluationRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Evaluation.DTO.EvaluationResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Mapper.EvaluationMapper;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Model.Evaluation;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Model.StatutEvaluation;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Repository.EvaluationRepository;
import com.example.Gestion_des_evaluations.Entity.Sujet.Event.AuditActionEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final ApplicationEventPublisher eventPublisher;

    public EvaluationService(EvaluationRepository evaluationRepository, ApplicationEventPublisher eventPublisher) {
        this.evaluationRepository = evaluationRepository;
        this.eventPublisher = eventPublisher;
    }

    // Crée une nouvelle évaluation après vérification des conflits de salle et d'horaire
    public EvaluationResponseDTO createEvaluation(EvaluationRequestDTO dto) {
        validateConflicts(dto, null);
        Evaluation evaluation = EvaluationMapper.toEntity(dto);
        evaluation.setStatut(StatutEvaluation.PROGRAMMEE);
        Evaluation saved = evaluationRepository.save(evaluation);

        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.CREATION_EVALUATION,
                "Création de l'évaluation " + saved.getId()
        ));

        return EvaluationMapper.toDTO(saved);
    }

    // Met à jour une évaluation existante en conservant la cohérence du planning
    public EvaluationResponseDTO updateEvaluation(Long id, EvaluationRequestDTO dto) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Évaluation introuvable avec l'id : " + id));

        if (evaluation.getStatut() == StatutEvaluation.VALIDEE) {
            throw new RuntimeException("Une évaluation validée ne peut plus être modifiée.");
        }

        validateConflicts(dto, id);
        EvaluationMapper.updateEntity(evaluation, dto);
        Evaluation saved = evaluationRepository.save(evaluation);

        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.MODIFICATION_EVALUATION,
                "Modification de l'évaluation " + saved.getId()
        ));

        return EvaluationMapper.toDTO(saved);
    }

    //changer statut
    public EvaluationResponseDTO changerStatut(Long id, StatutEvaluation statut) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Évaluation introuvable avec l'id : " + id));

        evaluation.setStatut(statut);
        Evaluation saved = evaluationRepository.save(evaluation);

        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.CHANGEMENT_STATUT_EVALUATION,
                "Changement de statut de l'évaluation " + saved.getId()
        ));

        return EvaluationMapper.toDTO(saved);
    }


    // Retourne une évaluation par identifiant
    public EvaluationResponseDTO getEvaluationById(Long id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Évaluation introuvable avec l'id : " + id));
        return EvaluationMapper.toDTO(evaluation);
    }

    // Retourne la liste complète des évaluations
    public List<EvaluationResponseDTO> getAllEvaluations() {
        return evaluationRepository.findAll()
                .stream()
                .map(EvaluationMapper::toDTO)
                .collect(Collectors.toList());
    }


    // Supprime une évaluation si elle existe
    public void deleteEvaluation(Long id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Évaluation introuvable avec l'id : " + id));

        if (evaluation.getStatut() == StatutEvaluation.VALIDEE) {
            throw new RuntimeException("Une évaluation validée ne peut plus être supprimée.");
        }

        evaluationRepository.delete(evaluation);

        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.SUPPRESSION_EVALUATION,
                "Suppression de l'évaluation " + id
        ));
    }

    // Vérifie qu'une salle n'est pas utilisée pour deux évaluations qui se chevauchent.
    private void validateConflicts(EvaluationRequestDTO dto, Long currentId) {
        boolean conflict = evaluationRepository
                .findBySalleAndDateEvaluation(dto.getSalle(), dto.getDateEvaluation())
                .stream()
                .anyMatch(ev ->
                        (currentId == null || !ev.getId().equals(currentId)) &&
                                dto.getHeureDebut().isBefore(ev.getHeureFin()) &&
                                dto.getHeureFin().isAfter(ev.getHeureDebut())
                );

        if (conflict) {
            throw new RuntimeException("Conflit détecté : cette salle est déjà utilisée sur ce créneau.");
        }
    }

    public List<EvaluationResponseDTO> getByProgrammationId(Long programmationId) {
        return evaluationRepository.findByProgrammationId(programmationId)
                .stream()
                .map(EvaluationMapper::toDTO)
                .toList();
    }

    public List<EvaluationResponseDTO> getByStatut(StatutEvaluation statut) {
        return evaluationRepository.findByStatut(statut)
                .stream()
                .map(EvaluationMapper::toDTO)
                .toList();
    }

    public List<EvaluationResponseDTO> getByEnseignantId(Long enseignantId) {
        return evaluationRepository.findByEnseignantId(enseignantId)
                .stream()
                .map(EvaluationMapper::toDTO)
                .toList();
    }

    public List<EvaluationResponseDTO> getByDateEvaluation(LocalDate dateEvaluation) {
        return evaluationRepository.findByDateEvaluation(dateEvaluation)
                .stream()
                .map(EvaluationMapper::toDTO)
                .toList();
    }
}
