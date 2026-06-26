package com.example.Gestion_des_evaluations.Entity.Note.Controller;

import com.example.Gestion_des_evaluations.Entity.Note.DTO.NoteRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Note.DTO.NoteResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Note.Service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base-url}/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<NoteResponseDTO> create(@RequestBody NoteRequestDTO dto) {
        return ResponseEntity.ok(noteService.deposerNote(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<List<NoteResponseDTO>> getAll() {
        return ResponseEntity.ok(noteService.listerToutesLesNotes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.chercherNoteParId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<NoteResponseDTO> update(@PathVariable Long id, @RequestBody NoteRequestDTO dto) {
        return ResponseEntity.ok(noteService.modifierNote(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/copie/{copieId}")
    public List<NoteResponseDTO> getByCopieId(@PathVariable Long copieId) {
        return noteService.getByCopieId(copieId);
    }


}