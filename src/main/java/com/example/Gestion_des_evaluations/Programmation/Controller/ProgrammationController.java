package com.example.Gestion_des_evaluations.Programmation.Controller;

import com.example.Gestion_des_evaluations.Programmation.DTO.*;
import com.example.Gestion_des_evaluations.Programmation.Model.Programmation;
import com.example.Gestion_des_evaluations.Programmation.Service.ProgrammationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/programmations")
@RequiredArgsConstructor
public class ProgrammationController {

    private final ProgrammationService programmationService;

    @PostMapping
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<ProgrammationResponseDTO> create(@RequestBody ProgrammationRequestDTO dto) {
        return ResponseEntity.ok(programmationService.creer(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<List<ProgrammationResponseDTO>> getAll() {
        return ResponseEntity.ok(programmationService.lister());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgrammationResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(programmationService.chercherParId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<ProgrammationResponseDTO> update(@PathVariable Long id, @RequestBody ProgrammationRequestDTO dto) {
        return ResponseEntity.ok(programmationService.modifier(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        programmationService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/evaluation/{evaluationId}")
    public List<ProgrammationResponseDTO> getByEvaluationId(@PathVariable Long evaluationId) {
        return programmationService.getByEvaluationId(evaluationId);
    }


    @GetMapping("/salle/{salle}")
    public ResponseEntity<List<Programmation>> getBySalle(@PathVariable String salle) {
        return ResponseEntity.ok(programmationService.getBySalle(salle));
    }

    @GetMapping("/date")
    public ResponseEntity<List<Programmation>> getByDateExamen(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateExamen) {
        return ResponseEntity.ok(programmationService.getByDateExamen(dateExamen));
    }

}