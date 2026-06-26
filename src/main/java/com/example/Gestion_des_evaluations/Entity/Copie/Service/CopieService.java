package com.example.Gestion_des_evaluations.Entity.Copie.Service;

import com.example.Gestion_des_evaluations.Entity.Action.Model.TypeAction;
import com.example.Gestion_des_evaluations.Entity.Copie.DTO.CopieRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Copie.DTO.CopieResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Copie.Mapper.CopieMapper;
import com.example.Gestion_des_evaluations.Entity.Copie.Model.Copie;
import com.example.Gestion_des_evaluations.Entity.Copie.Model.StatutCopie;
import com.example.Gestion_des_evaluations.Entity.Copie.Repository.CopieRepository;
import com.example.Gestion_des_evaluations.Entity.Sujet.Event.AuditActionEvent;
import com.example.Gestion_des_evaluations.Entity.Sujet.Model.SujetExamen;
import com.example.Gestion_des_evaluations.Entity.Sujet.Repository.SujetExamenRepository;
import com.example.Gestion_des_evaluations.Entity.Sujet.Service.FileStorageService;
import com.example.Gestion_des_evaluations.Entity.User.Model.User;
import com.example.Gestion_des_evaluations.Entity.User.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CopieService {

    private final CopieRepository copieRepository;
    private final SujetExamenRepository sujetExamenRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final ApplicationEventPublisher eventPublisher;

    public CopieResponseDTO enregistrerCopie(CopieRequestDTO dto, MultipartFile file) {
        SujetExamen sujet = sujetExamenRepository.findById(dto.getSujetExamenId())
                .orElseThrow(() -> new RuntimeException("Sujet introuvable"));

        User etudiant = userRepository.findById(dto.getEtudiantId())
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable"));

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Le fichier de la copie est obligatoire");
        }

        Copie copie = new Copie();
        copie.setSujetExamen(sujet);
        copie.setEtudiant(etudiant);
        copie.setFichierPath(fileStorageService.store(file));
        copie.setDateDepot(LocalDateTime.now());
        copie.setStatut(StatutCopie.DEPOSEE);
        copie.setNbreTotalCopie(1);

        Copie saved = copieRepository.save(copie);

        eventPublisher.publishEvent(new AuditActionEvent(
                etudiant.getId(),
                TypeAction.DEPOT_COPIE,
                "Dépôt de la copie " + saved.getId()
        ));

        return CopieMapper.toDTO(saved);
    }

    public List<CopieResponseDTO> listerToutesLesCopies() {
        return copieRepository.findAll()
                .stream()
                .map(CopieMapper::toDTO)
                .toList();
    }

    public CopieResponseDTO chercherCopieParId(Long id) {
        Copie copie = copieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Copie introuvable avec l'id : " + id));
        return CopieMapper.toDTO(copie);
    }

    public CopieResponseDTO updateCopie(Long id, CopieRequestDTO dto, MultipartFile file) {
        Copie copie = copieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Copie introuvable"));

        SujetExamen sujet = sujetExamenRepository.findById(dto.getSujetExamenId())
                .orElseThrow(() -> new RuntimeException("Sujet introuvable"));

        User etudiant = userRepository.findById(dto.getEtudiantId())
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable"));

        copie.setSujetExamen(sujet);
        copie.setEtudiant(etudiant);

        if (file != null && !file.isEmpty()) {
            copie.setFichierPath(fileStorageService.store(file));
        }

        Copie saved = copieRepository.save(copie);

        eventPublisher.publishEvent(new AuditActionEvent(
                etudiant.getId(),
                TypeAction.MODIFICATION_COPIE,
                "Modification de la copie " + saved.getId()
        ));

        return CopieMapper.toDTO(saved);
    }

    public void deleteCopie(Long id) {
        if (!copieRepository.existsById(id)) {
            throw new RuntimeException("Copie introuvable");
        }
        copieRepository.deleteById(id);

        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.SUPPRESSION_COPIE,
                "Suppression de la copie " + id
        ));
    }

    public CopieResponseDTO attribuerCopieAEnseignant(Long copieId, Long enseignantId) {
        Copie copie = copieRepository.findById(copieId)
                .orElseThrow(() -> new RuntimeException("Copie introuvable"));

        User enseignant = userRepository.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable"));

        boolean isEnseignant = enseignant.getRoles().stream()
                .anyMatch(role -> "ENSEIGNANT".equals(role.getName()));

        if (!isEnseignant) {
            throw new RuntimeException("L'utilisateur sélectionné n'a pas le rôle ENSEIGNANT");
        }

        copie.setStatut(StatutCopie.ATTRIBUEE);
        Copie saved = copieRepository.save(copie);

        eventPublisher.publishEvent(new AuditActionEvent(
                enseignant.getId(),
                TypeAction.ATTRIBUTION_COPIE,
                "Attribution de la copie " + saved.getId() + " à l'enseignant " + enseignant.getEmail()
        ));

        return CopieMapper.toDTO(saved);
    }
    @Transactional
    public CopieResponseDTO demanderRetraitCopie(Long id) {
        Copie copie = copieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Copie introuvable"));

        copie.setStatut(StatutCopie.RETIREE);
        copie.setDateRetrait(LocalDateTime.now());

        Copie saved = copieRepository.save(copie);

        eventPublisher.publishEvent(new AuditActionEvent(
                copie.getEtudiant().getId(),
                TypeAction.RETRAIT_COPIE,
                "Demande de retrait de la copie " + saved.getId()
        ));

        return CopieMapper.toDTO(saved);
    }
    @Transactional
    public CopieResponseDTO deposerCopieCorrigee(Long id, MultipartFile file) {
        Copie copie = copieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Copie introuvable"));

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Le fichier corrigé est obligatoire");
        }

        copie.setFichierPath(fileStorageService.store(file));
        copie.setStatut(StatutCopie.CORRIGEE);
        copie.setDateRetour(LocalDateTime.now());

        Copie saved = copieRepository.save(copie);

        eventPublisher.publishEvent(new AuditActionEvent(
                null,
                TypeAction.DEPOT_COPIE_CORRIGEE,
                "Dépôt de la copie corrigée " + saved.getId()
        ));

        return CopieMapper.toDTO(saved);
    }

    public List<CopieResponseDTO> getBySujetExamenId(Long sujetExamenId) {
        return copieRepository.findBySujetExamenId(sujetExamenId)
                .stream()
                .map(CopieMapper::toDTO)
                .toList();
    }

    public List<CopieResponseDTO> getByStatut(StatutCopie statut) {
        return copieRepository.findByStatut(statut)
                .stream()
                .map(CopieMapper::toDTO)
                .toList();
    }

    public List<CopieResponseDTO> getByEtudiantAndStatut(Long etudiantId, StatutCopie statut) {
        return copieRepository.findByEtudiantIdAndStatut(etudiantId, statut)
                .stream()
                .map(CopieMapper::toDTO)
                .toList();
    }

    public List<CopieResponseDTO> getBySujetAndStatut(Long sujetExamenId, StatutCopie statut) {
        return copieRepository.findBySujetExamenIdAndStatut(sujetExamenId, statut)
                .stream()
                .map(CopieMapper::toDTO)
                .toList();
    }

    public void affecterCorrecteur(Long copieId, Long userId) {
        Copie copie = copieRepository.findById(copieId)
                .orElseThrow(() -> new RuntimeException("Copie introuvable"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User introuvable"));

        if (user.getRoles().stream().noneMatch(r -> r.getName().equals("CORRECTEUR"))) {
            throw new RuntimeException("Ce user n'est pas correcteur");
        }

        copie.setCorrecteur(user);
        copieRepository.save(copie);
    }
}