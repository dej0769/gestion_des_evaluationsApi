package com.example.Gestion_des_evaluations.Entity.Incident.Controller;

import com.example.Gestion_des_evaluations.Entity.Incident.DTO.IncidentRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Incident.DTO.IncidentResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Incident.Model.IncidentStatut;
import com.example.Gestion_des_evaluations.Entity.Incident.Model.IncidentType;
import com.example.Gestion_des_evaluations.Entity.Incident.Service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("${app.base-url}/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @PostMapping
    @PreAuthorize("hasRole('SURVEILLANT') or hasRole('AGENT_SCOLARITE') ")
    public ResponseEntity<IncidentResponseDTO> signaler(@RequestBody IncidentRequestDTO dto) {
        return ResponseEntity.ok(incidentService.signaler(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE') or hasRole('AGENT_SCOLARITE')")
    public ResponseEntity<List<IncidentResponseDTO>> liste() {
        return ResponseEntity.ok(incidentService.liste());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE') or hasRole('AGENT_SCOLARITE')")
    public ResponseEntity<IncidentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getById(id));
    }

    @PatchMapping("/{id}/traiter")
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<IncidentResponseDTO> traiter(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.traiter(id));
    }

    @PatchMapping("/{id}/cloturer")
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<IncidentResponseDTO> cloturer(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.cloturer(id));
    }

    @GetMapping("/evaluation/{evaluationId}")
    public List<IncidentResponseDTO> getByEvaluationId(@PathVariable Long evaluationId) {
        return incidentService.getByEvaluationId(evaluationId);
    }

    @GetMapping("/statut/{statut}")
    public List<IncidentResponseDTO> getByStatut(@PathVariable IncidentStatut statut) {
        return incidentService.getByStatut(statut);
    }

    @GetMapping("/type/{type}")
    public List<IncidentResponseDTO> getByType(@PathVariable IncidentType type) {
        return incidentService.getByType(type);
    }

    @GetMapping("/date")
    public List<IncidentResponseDTO> getByDateSignalement(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateSignalement) {
        return incidentService.getByDateSignalement(dateSignalement);
    }
}