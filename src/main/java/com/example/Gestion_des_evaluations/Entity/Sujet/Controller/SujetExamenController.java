package com.example.Gestion_des_evaluations.Entity.Sujet.Controller;

import com.example.Gestion_des_evaluations.Entity.Sujet.DTO.SujetExamenRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Sujet.DTO.SujetExamenResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Sujet.Model.StatutSujetExamen;
import com.example.Gestion_des_evaluations.Entity.Sujet.Model.SujetExamen;
import com.example.Gestion_des_evaluations.Entity.Sujet.Service.SujetExamenService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("${app.base-url}/sujets-examen")
public class SujetExamenController {

    private final SujetExamenService sujetExamenService;
    private final ObjectMapper objectMapper;


    public SujetExamenController(SujetExamenService sujetExamenService, ObjectMapper objectMapper) {
        this.sujetExamenService = sujetExamenService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<SujetExamenResponseDTO> create(
            @RequestPart("data") String data,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            SujetExamenRequestDTO dto = objectMapper.readValue(data, SujetExamenRequestDTO.class);
            return ResponseEntity.ok(sujetExamenService.create(dto, file));
        } catch (Exception e) {
            throw new RuntimeException("JSON invalide", e);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SujetExamenResponseDTO> update(
            @PathVariable Long id,
            @RequestPart("data") String data,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            SujetExamenRequestDTO dto = objectMapper.readValue(data, SujetExamenRequestDTO.class);
            return ResponseEntity.ok(sujetExamenService.update(id, dto, file));
        } catch (Exception e) {
            throw new RuntimeException("JSON invalide", e);
        }
    }

    @PutMapping("/{id}/valider")
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<SujetExamen> valider(@PathVariable Long id) {
        return ResponseEntity.ok(sujetExamenService.valider(id));
    }

    @PutMapping("/{id}/rejeter")
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<SujetExamen> rejeter(@PathVariable Long id,
                                               @RequestParam String motif) {
        return ResponseEntity.ok(sujetExamenService.rejeter(id, motif));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RESPONSABLE_PEDAGOGIQUE','AGENT_SCOLARITE','ENSEIGNANT')")
    public ResponseEntity<SujetExamenResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sujetExamenService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('RESPONSABLE_PEDAGOGIQUE','AGENT_SCOLARITE','ENSEIGNANT')")
    public ResponseEntity<List<SujetExamenResponseDTO>> getAll() {
        return ResponseEntity.ok(sujetExamenService.getAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RESPONSABLE_PEDAGOGIQUE','ENSEIGNANT')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sujetExamenService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/archiver")
    @PreAuthorize("hasRole('RESPONSABLE_PEDAGOGIQUE')")
    public ResponseEntity<SujetExamenResponseDTO> archive(@PathVariable Long id) {
        return ResponseEntity.ok(sujetExamenService.archive(id));
    }

    @GetMapping("/enseignant/{enseignantId}")
    public List<SujetExamenResponseDTO> getByEnseignantId(@PathVariable Long enseignantId) {
        return sujetExamenService.getByEnseignantId(enseignantId);
    }

    @GetMapping("/statut/{statut}")
    public List<SujetExamenResponseDTO> getByStatut(@PathVariable StatutSujetExamen statut) {
        return sujetExamenService.getByStatut(statut);
    }

    @GetMapping("/evaluation/{evaluationId}")
    public List<SujetExamenResponseDTO> getByEvaluationId(@PathVariable Long evaluationId) {
        return sujetExamenService.getByEvaluationId(evaluationId);
    }


}