package com.example.Gestion_des_evaluations.Entity.Evaluation.Controller;

import com.example.Gestion_des_evaluations.Entity.Evaluation.DTO.EvaluationRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Evaluation.DTO.EvaluationResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Model.StatutEvaluation;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Service.EvaluationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("${app.base-url}/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }
    // Créer une nouvelle programmation d'évaluation
    @PostMapping
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<EvaluationResponseDTO> createEvaluation(@RequestBody EvaluationRequestDTO dto) {
        return new ResponseEntity<>(evaluationService.createEvaluation(dto), HttpStatus.CREATED);
    }

    // Modifier une évaluation existante
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<EvaluationResponseDTO> updateEvaluation(@PathVariable Long id, @RequestBody EvaluationRequestDTO dto
    ) {
        return ResponseEntity.ok(evaluationService.updateEvaluation(id, dto));
    }
//changer statut ou valider une programmation

    @PutMapping("/{id}/statut")
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<EvaluationResponseDTO> changerStatut(
            @PathVariable Long id,
            @RequestParam StatutEvaluation statut) {
        return ResponseEntity.ok(evaluationService.changerStatut(id, statut));
    }



    // Consulter une évaluation par son identifiant
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<EvaluationResponseDTO> getEvaluationById(@PathVariable Long id) {
        return ResponseEntity.ok(evaluationService.getEvaluationById(id));
    }

    // Lister toutes les évaluations programmées
    @GetMapping
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<List<EvaluationResponseDTO>> getAllEvaluations() {
        return ResponseEntity.ok(evaluationService.getAllEvaluations());
    }

    // Supprimer une programmation d'évaluation
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteEvaluation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/programmation/{programmationId}")
    public List<EvaluationResponseDTO> getByProgrammationId(@PathVariable Long programmationId) {
        return evaluationService.getByProgrammationId(programmationId);
    }

    @GetMapping("/statut/{statut}")
    public List<EvaluationResponseDTO> getByStatut(@PathVariable StatutEvaluation statut) {
        return evaluationService.getByStatut(statut);
    }

    @GetMapping("/enseignant/{enseignantId}")
    public List<EvaluationResponseDTO> getByEnseignantId(@PathVariable Long enseignantId) {
        return evaluationService.getByEnseignantId(enseignantId);
    }

    @GetMapping("/date")
    public List<EvaluationResponseDTO> getByDateEvaluation(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEvaluation) {
        return evaluationService.getByDateEvaluation(dateEvaluation);
    }

    @PutMapping("/{evaluationId}/surveillants")
    @PreAuthorize("hasRole('AGENT_SCOLARITE')")
    public ResponseEntity<String> affecterSurveillants(
            @PathVariable Long evaluationId,
            @RequestBody Set<Long> userIds) {

        evaluationService.affecterSurveillants(evaluationId, userIds);
        return ResponseEntity.ok("Surveillants affectés avec succès");
    }
}