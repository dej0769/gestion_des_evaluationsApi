package com.example.Gestion_des_evaluations.Entity.Reclamation.Service;

import com.example.Gestion_des_evaluations.Entity.Action.Model.TypeAction;
import com.example.Gestion_des_evaluations.Entity.Reclamation.DTO.SessionReclamationDTO;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Mapper.SessionReclamationMapper;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.SessionReclamation;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.SessionStatut;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Repository.SessionReclamationRepository;
import com.example.Gestion_des_evaluations.Entity.Sujet.Event.AuditActionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionReclamationService {

    private final SessionReclamationRepository sessionReclamationRepository;
    private final ApplicationEventPublisher eventPublisher;


    public SessionReclamationDTO creerSession() {
        SessionReclamation session = new SessionReclamation();
        session.setStatut(SessionStatut.FERMEE);
        SessionReclamation saved = sessionReclamationRepository.save(session);

        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.CREATION_SESSION_RECLAMATION,
                "Création de la session de réclamation " + saved.getId()
        ));

        return SessionReclamationMapper.toDTO(saved);
    }

    public SessionReclamationDTO ouvrirSession(Long id) {
        SessionReclamation session = sessionReclamationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session introuvable"));
        session.setStatut(SessionStatut.OUVERTE);
        SessionReclamation saved = sessionReclamationRepository.save(session);

        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.OUVERTURE_SESSION_RECLAMATION,
                "Ouverture de la session de réclamation " + saved.getId()
        ));

        return SessionReclamationMapper.toDTO(saved);
    }

    public SessionReclamationDTO fermerSession(Long id) {
        SessionReclamation session = sessionReclamationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session introuvable"));
        session.setStatut(SessionStatut.FERMEE);
        SessionReclamation saved = sessionReclamationRepository.save(session);

        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.FERMETURE_SESSION_RECLAMATION,
                "Fermeture de la session de réclamation " + saved.getId()
        ));

        return SessionReclamationMapper.toDTO(saved);
    }
}