package com.example.Gestion_des_evaluations.Note.Service;

import com.example.Gestion_des_evaluations.Action.Model.TypeAction;
import com.example.Gestion_des_evaluations.Copy.Model.Copie;
import com.example.Gestion_des_evaluations.Copy.Model.StatutCopie;
import com.example.Gestion_des_evaluations.Copy.Repository.CopieRepository;
import com.example.Gestion_des_evaluations.Note.DTO.NoteRequestDTO;
import com.example.Gestion_des_evaluations.Note.DTO.NoteResponseDTO;
import com.example.Gestion_des_evaluations.Note.Mapper.NoteMapper;
import com.example.Gestion_des_evaluations.Note.Model.Note;
import com.example.Gestion_des_evaluations.Note.Repository.NoteRepository;
import com.example.Gestion_des_evaluations.Subject.Event.AuditActionEvent;
import com.example.Gestion_des_evaluations.User.Model.User;
import com.example.Gestion_des_evaluations.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final CopieRepository copieRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public NoteResponseDTO deposerNote(NoteRequestDTO dto) {
        Copie copie = copieRepository.findById(dto.getCopieId())
                .orElseThrow(() -> new RuntimeException("Copie introuvable"));

        User correcteur = userRepository.findById(dto.getCorrecteurId())
                .orElseThrow(() -> new RuntimeException("Correcteur introuvable"));

        Note note = new Note();
        note.setValeur(dto.getValeur());
        note.setCommentaire(dto.getCommentaire());
        note.setDateCorrection(LocalDateTime.now());
        note.setNbreEtudiantsAyantLaMoyenne(dto.getNbreEtudiantsAyantLaMoyenne());
        note.setNbreTotalEtudiantsComposes(dto.getNbreTotalEtudiantsComposes());
        note.setCopie(copie);
        note.setCorrecteur(correcteur);

        copie.setStatut(StatutCopie.CORRIGEE);
        copieRepository.save(copie);

        Note saved = noteRepository.save(note);

        eventPublisher.publishEvent(new AuditActionEvent(
                correcteur.getId(),
                TypeAction.DEPOT_NOTE,
                "Dépôt de la note pour la copie " + copie.getId()
        ));

        return NoteMapper.toDTO(saved);
    }

    public List<NoteResponseDTO> listerToutesLesNotes() {
        return noteRepository.findAll()
                .stream()
                .map(NoteMapper::toDTO)
                .toList();
    }

    public NoteResponseDTO chercherNoteParId(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note introuvable avec l'id : " + id));
        return NoteMapper.toDTO(note);
    }

    public NoteResponseDTO modifierNote(Long id, NoteRequestDTO dto) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note introuvable"));

        Copie copie = copieRepository.findById(dto.getCopieId())
                .orElseThrow(() -> new RuntimeException("Copie introuvable"));

        User correcteur = userRepository.findById(dto.getCorrecteurId())
                .orElseThrow(() -> new RuntimeException("Correcteur introuvable"));

        note.setValeur(dto.getValeur());
        note.setCommentaire(dto.getCommentaire());
        note.setDateCorrection(LocalDateTime.now());
        note.setCopie(copie);
        note.setCorrecteur(correcteur);

        Note saved = noteRepository.save(note);

        eventPublisher.publishEvent(new AuditActionEvent(
                correcteur.getId(),
                TypeAction.MODIFICATION_NOTE,
                "Modification de la note " + saved.getId()
        ));

        return NoteMapper.toDTO(saved);
    }

    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new RuntimeException("Note introuvable");
        }
        noteRepository.deleteById(id);

        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.SUPPRESSION_NOTE,
                "Suppression de la note " + id
        ));
    }

    public List<NoteResponseDTO> getByCopieId(Long copieId) {
        return noteRepository.findByCopieId(copieId)
                .stream()
                .map(NoteMapper::toDTO)
                .toList();
    }

    public List<NoteResponseDTO> getByCorrecteurId(Long correcteurId) {
        return noteRepository.findByCorrecteurId(correcteurId)
                .stream()
                .map(NoteMapper::toDTO)
                .toList();
    }


    public List<NoteResponseDTO> getByEtudiantId(Long etudiantId) {
        return noteRepository.findByCopieEtudiantId(etudiantId)
                .stream()
                .map(NoteMapper::toDTO)
                .toList();
    }
}