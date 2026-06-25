package com.example.Gestion_des_evaluations.Entity.Copie.Controller;

import com.example.Gestion_des_evaluations.Entity.Copie.DTO.CopieRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Copie.DTO.CopieResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Copie.Model.StatutCopie;
import com.example.Gestion_des_evaluations.Entity.Copie.Service.CopieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("${app.base-url}/copies")
@RequiredArgsConstructor
public class CopieController {

    private final CopieService copieService;



    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<CopieResponseDTO> create(
            @RequestPart("data") String data,
            @RequestPart("file") MultipartFile file
    ) throws Exception {
        CopieRequestDTO dto = new ObjectMapper().readValue(data, CopieRequestDTO.class);
        return ResponseEntity.ok(copieService.enregistrerCopie(dto, file));
    }

    @GetMapping
    public ResponseEntity<List<CopieResponseDTO>> getAll() {
        return ResponseEntity.ok(copieService.listerToutesLesCopies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CopieResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(copieService.chercherCopieParId(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<CopieResponseDTO> update(
            @PathVariable Long id,
            @RequestPart("data") String data,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws Exception {
        CopieRequestDTO dto = new ObjectMapper().readValue(data, CopieRequestDTO.class);
        return ResponseEntity.ok(copieService.updateCopie(id, dto, file));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        copieService.deleteCopie(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/attribuer/{enseignantId}")
    @PreAuthorize("hasRole('AGENT_SCOLARITE')")
    public ResponseEntity<CopieResponseDTO> attribuer(
            @PathVariable Long id,
            @PathVariable Long enseignantId
    ) {
        return ResponseEntity.ok(copieService.attribuerCopieAEnseignant(id, enseignantId));
    }

    @PutMapping("/{id}/retirer")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<CopieResponseDTO> retirer(@PathVariable Long id) {
        return ResponseEntity.ok(copieService.demanderRetraitCopie(id));
    }

    @PutMapping("/{id}/corrigee")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<CopieResponseDTO> deposerCorrigee(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file
    ) {
        return ResponseEntity.ok(copieService.deposerCopieCorrigee(id, file));
    }

    @PutMapping("/{id}/en-correction")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<CopieResponseDTO> enCorrection(@PathVariable Long id) {
        return ResponseEntity.ok(copieService.mettreEnCorrection(id));
    }

    @GetMapping("/sujet/{sujetExamenId}")
    public List<CopieResponseDTO> getBySujetExamenId(@PathVariable Long sujetExamenId) {
        return copieService.getBySujetExamenId(sujetExamenId);
    }

    @GetMapping("/etudiant/{etudiantId}")
    public List<CopieResponseDTO> getByEtudiantId(@PathVariable Long etudiantId) {
        return copieService.getByEtudiantId(etudiantId);
    }

    @GetMapping("/statut/{statut}")
    public List<CopieResponseDTO> getByStatut(@PathVariable StatutCopie statut) {
        return copieService.getByStatut(statut);
    }

    @GetMapping("/etudiant/{etudiantId}/statut/{statut}")
    public List<CopieResponseDTO> getByEtudiantAndStatut(
            @PathVariable Long etudiantId,
            @PathVariable StatutCopie statut) {
        return copieService.getByEtudiantAndStatut(etudiantId, statut);
    }

    @GetMapping("/sujet/{sujetExamenId}/statut/{statut}")
    public List<CopieResponseDTO> getBySujetAndStatut(
            @PathVariable Long sujetExamenId,
            @PathVariable StatutCopie statut) {
        return copieService.getBySujetAndStatut(sujetExamenId, statut);
    }
}