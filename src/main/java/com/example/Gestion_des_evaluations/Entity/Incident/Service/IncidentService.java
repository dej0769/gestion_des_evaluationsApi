package com.example.Gestion_des_evaluations.Entity.Incident.Service;

import com.example.Gestion_des_evaluations.Entity.Action.Model.TypeAction;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Model.Evaluation;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Repository.EvaluationRepository;
import com.example.Gestion_des_evaluations.Entity.Incident.DTO.IncidentRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Incident.DTO.IncidentResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Incident.Mapper.IncidentMapper;
import com.example.Gestion_des_evaluations.Entity.Incident.Model.Incident;
import com.example.Gestion_des_evaluations.Entity.Incident.Model.IncidentStatut;
import com.example.Gestion_des_evaluations.Entity.Incident.Model.IncidentType;
import com.example.Gestion_des_evaluations.Entity.Incident.Repository.IncidentRepository;
import com.example.Gestion_des_evaluations.Entity.Sujet.Event.AuditActionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final EvaluationRepository evaluationRepository;
    private final ApplicationEventPublisher eventPublisher;

    public IncidentResponseDTO signaler(IncidentRequestDTO dto) {
        Evaluation evaluation = evaluationRepository.findById(dto.getEvaluationId())
                .orElseThrow(() -> new RuntimeException("Evaluation introuvable"));

        Incident incident = new Incident();
        incident.setDateSignalement(dto.getDateSignalement());
        incident.setDescription(dto.getDescription());
        incident.setType(dto.getType());
        incident.setStatut(IncidentStatut.SIGNALE);
        incident.setEvaluation(evaluation);

        Incident saved = incidentRepository.save(incident);

        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.SIGNALER_INCIDENT,
                "Signalement de l'incident " + saved.getId()
        ));

        return IncidentMapper.toDTO(saved);
    }

    public IncidentResponseDTO traiter(Long id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident introuvable"));

        incident.setStatut(IncidentStatut.TRAITE);
        Incident saved = incidentRepository.save(incident);

        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.TRAITER_INCIDENT,
                "Traitement de l'incident " + saved.getId()
        ));

        return IncidentMapper.toDTO(saved);
    }

    public IncidentResponseDTO cloturer(Long id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident introuvable"));

        incident.setStatut(IncidentStatut.CLOTURE);
        Incident saved = incidentRepository.save(incident);

        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.CLOTURER_INCIDENT,
                "Clôture de l'incident " + saved.getId()
        ));

        return IncidentMapper.toDTO(saved);
    }

    public List<IncidentResponseDTO> liste() {
        return incidentRepository.findAll()
                .stream()
                .map(IncidentMapper::toDTO)
                .toList();
    }

    public IncidentResponseDTO getById(Long id) {
        return IncidentMapper.toDTO(
                incidentRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Incident introuvable"))
        );
    }

    public List<IncidentResponseDTO> getByEvaluationId(Long evaluationId) {
        return incidentRepository.findByEvaluationId(evaluationId)
                .stream()
                .map(IncidentMapper::toDTO)
                .toList();
    }

    public List<IncidentResponseDTO> getByStatut(IncidentStatut statut) {
        return incidentRepository.findByStatut(statut)
                .stream()
                .map(IncidentMapper::toDTO)
                .toList();
    }

    public List<IncidentResponseDTO> getByType(IncidentType type) {
        return incidentRepository.findByType(type)
                .stream()
                .map(IncidentMapper::toDTO)
                .toList();
    }

    public List<IncidentResponseDTO> getByDateSignalement(LocalDate dateSignalement) {
        LocalDateTime debut = dateSignalement.atStartOfDay();
        LocalDateTime fin = dateSignalement.atTime(LocalTime.MAX);

        return incidentRepository.findByDateSignalementBetween(debut, fin)
                .stream()
                .map(IncidentMapper::toDTO)
                .toList();
    }
}
