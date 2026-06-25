package com.example.Gestion_des_evaluations.Entity.Programmation.Service;

import com.example.Gestion_des_evaluations.Entity.Action.Model.TypeAction;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Model.Evaluation;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Repository.EvaluationRepository;
import com.example.Gestion_des_evaluations.Entity.Programmation.DTO.ProgrammationRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Programmation.DTO.ProgrammationResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Programmation.Mapper.ProgrammationMapper;
import com.example.Gestion_des_evaluations.Entity.Programmation.Model.Programmation;
import com.example.Gestion_des_evaluations.Entity.Programmation.Repository.ProgrammationRepository;
import com.example.Gestion_des_evaluations.Entity.Sujet.Event.AuditActionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgrammationService {

    private final ProgrammationRepository programmationRepository;
    private final EvaluationRepository evaluationRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ProgrammationResponseDTO creer(ProgrammationRequestDTO dto) {
        Evaluation evaluation = evaluationRepository.findById(dto.getEvaluationId())
                .orElseThrow(() -> new RuntimeException("Evaluation introuvable"));

        Programmation programmation = new Programmation();
        programmation.setDateExamen(dto.getDateExamen());
        programmation.setHeureDebut(dto.getHeureDebut());
        programmation.setHeureFin(dto.getHeureFin());
        programmation.setSalle(dto.getSalle());
        programmation.setStatut(dto.getStatut());
        programmation.setEvaluation(evaluation);

        Programmation saved = programmationRepository.save(programmation);

        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.CREATION_PROGRAMMATION,
                "Création de la programmation " + saved.getId()
        ));

        return ProgrammationMapper.toDTO(saved);
    }

    public List<ProgrammationResponseDTO> lister() {
        return programmationRepository.findAll()
                .stream()
                .map(ProgrammationMapper::toDTO)
                .toList();
    }

    public ProgrammationResponseDTO chercherParId(Long id) {
        Programmation programmation = programmationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programmation introuvable"));
        return ProgrammationMapper.toDTO(programmation);
    }

    public ProgrammationResponseDTO modifier(Long id, ProgrammationRequestDTO dto) {
        Programmation programmation = programmationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programmation introuvable"));

        Evaluation evaluation = evaluationRepository.findById(dto.getEvaluationId())
                .orElseThrow(() -> new RuntimeException("Evaluation introuvable"));

        programmation.setDateExamen(dto.getDateExamen());
        programmation.setHeureDebut(dto.getHeureDebut());
        programmation.setHeureFin(dto.getHeureFin());
        programmation.setSalle(dto.getSalle());
        programmation.setStatut(dto.getStatut());
        programmation.setEvaluation(evaluation);

        Programmation saved = programmationRepository.save(programmation);

        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.MODIFICATION_PROGRAMMATION,
                "Modification de la programmation " + saved.getId()
        ));

        return ProgrammationMapper.toDTO(saved);
    }

    public void supprimer(Long id) {
        Programmation programmation = programmationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programmation introuvable"));

        programmationRepository.delete(programmation);

        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.SUPPRESSION_PROGRAMMATION,
                "Suppression de la programmation " + id
        ));
    }

    public List<ProgrammationResponseDTO> getByEvaluationId(Long evaluationId) {
        return programmationRepository.findByEvaluationId(evaluationId)
                .stream()
                .map(ProgrammationMapper::toDTO)
                .toList();
    }

    public List<Programmation> getBySalle(String salle) {
        return programmationRepository.findBySalle(salle);
    }

    public List<Programmation> getByDateExamen(LocalDate dateExamen) {
        return programmationRepository.findByDateExamen(dateExamen);
    }
}