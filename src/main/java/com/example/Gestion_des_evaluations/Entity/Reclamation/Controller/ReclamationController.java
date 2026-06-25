package com.example.Gestion_des_evaluations.Entity.Reclamation.Controller;

import com.example.Gestion_des_evaluations.Entity.Reclamation.DTO.ReclamationRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Reclamation.DTO.ReclamationResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Reclamation.DTO.SessionReclamationDTO;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.ReclamationStatut;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Service.ReclamationService;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Service.SessionReclamationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("${app.base-url}")
@RequiredArgsConstructor
public class ReclamationController {

    private final ReclamationService reclamationService;
    private final SessionReclamationService sessionReclamationService;

    @PostMapping(value = "/reclamations", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<ReclamationResponseDTO> deposer(@ModelAttribute ReclamationRequestDTO dto) {
        return ResponseEntity.ok(reclamationService.deposer(dto));
    }

    @GetMapping("/reclamations/{id}")
    @PreAuthorize("hasRole('ETUDIANT') or hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<ReclamationResponseDTO> suivre(@PathVariable Long id) {
        return ResponseEntity.ok(reclamationService.suivre(id));
    }

    @PatchMapping("/reclamations/{id}/traiter")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<ReclamationResponseDTO> traiter(@PathVariable Long id) {
        return ResponseEntity.ok(reclamationService.traiter(id));
    }

    @DeleteMapping("/reclamations/{id}")
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        reclamationService.supprimer(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/sessions-reclamations")
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<SessionReclamationDTO> creerSession() {
        return ResponseEntity.ok(sessionReclamationService.creerSession());
    }

    @PatchMapping("/sessions-reclamations/{id}/ouvrir")
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<SessionReclamationDTO> ouvrirSession(@PathVariable Long id) {
        return ResponseEntity.ok(sessionReclamationService.ouvrirSession(id));
    }

    @PatchMapping("/sessions-reclamations/{id}/fermer")
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<SessionReclamationDTO> fermerSession(@PathVariable Long id) {
        return ResponseEntity.ok(sessionReclamationService.fermerSession(id));
    }

    @GetMapping("/note/{noteId}")
    public List<ReclamationResponseDTO> getByNoteId(@PathVariable Long noteId) {
        return reclamationService.getByNoteId(noteId);
    }

    @GetMapping("/statut/{statut}")
    public List<ReclamationResponseDTO> getByStatut(@PathVariable ReclamationStatut statut) {
        return reclamationService.getByStatut(statut);
    }

    @GetMapping("/reclamations")
    public ResponseEntity<List<ReclamationResponseDTO>> getAll() {
        return ResponseEntity.ok(reclamationService.getAllReclamations());
    }



    @GetMapping("/date")
    public ResponseEntity<List<ReclamationResponseDTO>> getByDateCreation(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateCreation) {
        return ResponseEntity.ok(reclamationService.getByDateCreation(dateCreation));
    }

    @GetMapping("/programmation/{programmationId}")
    public List<ReclamationResponseDTO> getByProgrammationId(@PathVariable Long programmationId) {
        return reclamationService.getByProgrammationId(programmationId);
    }

    @GetMapping("/session/{sessionId}")
    public List<ReclamationResponseDTO> getBySessionReclamationId(@PathVariable Long sessionId) {
        return reclamationService.getBySessionReclamationId(sessionId);
    }
}