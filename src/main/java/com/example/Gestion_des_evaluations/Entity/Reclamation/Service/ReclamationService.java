package com.example.Gestion_des_evaluations.Entity.Reclamation.Service;

import com.example.Gestion_des_evaluations.Entity.Action.Model.TypeAction;
import com.example.Gestion_des_evaluations.Entity.Note.Model.Note;
import com.example.Gestion_des_evaluations.Entity.Note.Repository.NoteRepository;
import com.example.Gestion_des_evaluations.Entity.Reclamation.DTO.ReclamationRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Reclamation.DTO.ReclamationResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Mapper.ReclamationMapper;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.Reclamation;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.ReclamationStatut;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.SessionReclamation;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.SessionStatut;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Repository.ReclamationRepository;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Repository.SessionReclamationRepository;
import com.example.Gestion_des_evaluations.Entity.Sujet.Event.AuditActionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReclamationService {

    private final ReclamationRepository reclamationRepository;
    private final NoteRepository noteRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SessionReclamationRepository sessionReclamationRepository;

    public ReclamationResponseDTO deposer(ReclamationRequestDTO dto) {
        SessionReclamation session = sessionReclamationRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session introuvable"));

        if (session.getStatut() != SessionStatut.OUVERTE) {
            throw new RuntimeException("La session de réclamation est fermée");
        }

        Note note = noteRepository.findById(dto.getNoteId())
                .orElseThrow(() -> new RuntimeException("Note introuvable"));

        Reclamation r = new Reclamation();
        r.setDateCreation(dto.getDateCreation());
        r.setDescription(dto.getDescription());
        r.setFichier(dto.getFichier() != null ? dto.getFichier().getOriginalFilename() : null);
        r.setObjet(dto.getObjet());
        r.setTypeDocument(dto.getTypeDocument());
        r.setStatut(ReclamationStatut.DEPOSEE);
        r.setNote(note);
        r.setSessionReclamation(session);

        Reclamation saved = reclamationRepository.save(r);

        eventPublisher.publishEvent(new AuditActionEvent(
                note.getCorrecteur().getId(),
                TypeAction.DEPOT_RECLAMATION,
                "Dépôt de la réclamation " + saved.getId()
        ));

        return ReclamationMapper.toDTO(saved);
    }

    public ReclamationResponseDTO traiter(Long id) {
        Reclamation r = getByIdEntity(id);
        r.setStatut(ReclamationStatut.TRAITEE);
        Reclamation saved = reclamationRepository.save(r);

        eventPublisher.publishEvent(new AuditActionEvent(
                saved.getNote().getCorrecteur().getId(),
                TypeAction.TRAITEMENT_RECLAMATION,
                "Traitement de la réclamation " + saved.getId()
        ));

        return ReclamationMapper.toDTO(saved);
    }

    public ReclamationResponseDTO suivre(Long id) {
        return ReclamationMapper.toDTO(getByIdEntity(id));
    }

    public void supprimer(Long id) {
        Reclamation r = getByIdEntity(id);

        if (r.getStatut() == ReclamationStatut.TRAITEE) {
            throw new RuntimeException("Impossible de supprimer une réclamation traitée");
        }

        reclamationRepository.delete(r);
        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.SUPPRESSION_RECLAMATION,
                "Suppression de la réclamation " + id
        ));
    }

    private Reclamation getByIdEntity(Long id) {
        return reclamationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réclamation introuvable"));
    }

    public List<ReclamationResponseDTO> getByNoteId(Long noteId) {
        return reclamationRepository.findByNoteId(noteId)
                .stream()
                .map(ReclamationMapper::toDTO)
                .toList();
    }

    public List<ReclamationResponseDTO> getByStatut(ReclamationStatut statut) {
        return reclamationRepository.findByStatut(statut)
                .stream()
                .map(ReclamationMapper::toDTO)
                .toList();
    }

    public List<ReclamationResponseDTO> getAllReclamations() {
        return reclamationRepository.findAll()
                .stream()
                .map(ReclamationMapper::toDTO)
                .toList();
    }


    public List<ReclamationResponseDTO> getByDateCreation(LocalDate dateCreation) {
        return reclamationRepository.findByDateCreation(dateCreation)
                .stream()
                .map(ReclamationMapper::toDTO)
                .toList();
    }

    public List<ReclamationResponseDTO> getByProgrammationId(Long programmationId) {
        return reclamationRepository.findByProgrammationId(programmationId)
                .stream()
                .map(ReclamationMapper::toDTO)
                .toList();
    }

    public List<ReclamationResponseDTO> getBySessionReclamationId(Long sessionId) {
        return reclamationRepository.findBySessionReclamationId(sessionId)
                .stream()
                .map(ReclamationMapper::toDTO)
                .toList();
    }
}