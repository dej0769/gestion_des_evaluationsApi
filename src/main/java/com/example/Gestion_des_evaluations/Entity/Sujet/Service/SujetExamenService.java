package com.example.Gestion_des_evaluations.Entity.Sujet.Service;

import com.example.Gestion_des_evaluations.Entity.Action.Model.TypeAction;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Model.Evaluation;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Repository.EvaluationRepository;
import com.example.Gestion_des_evaluations.Entity.Sujet.DTO.SujetExamenActionResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Sujet.DTO.SujetExamenRequestDTO;
import com.example.Gestion_des_evaluations.Entity.Sujet.DTO.SujetExamenResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Sujet.Event.AuditActionEvent;
import com.example.Gestion_des_evaluations.Entity.Sujet.Event.SujetRejeteEvent;
import com.example.Gestion_des_evaluations.Entity.Sujet.Event.SujetValideEvent;
import com.example.Gestion_des_evaluations.Entity.Sujet.Mapper.SujetExamenActionMapper;
import com.example.Gestion_des_evaluations.Entity.Sujet.Mapper.SujetExamenMapper;
import com.example.Gestion_des_evaluations.Entity.Sujet.Model.StatutSujetExamen;
import com.example.Gestion_des_evaluations.Entity.Sujet.Model.SujetExamen;
import com.example.Gestion_des_evaluations.Entity.Sujet.Repository.SujetExamenRepository;
import com.example.Gestion_des_evaluations.Entity.User.Model.User;
import com.example.Gestion_des_evaluations.Entity.User.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.context.ApplicationEventPublisher;



import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SujetExamenService {

    private final SujetExamenRepository sujetExamenRepository;
    private final FileStorageService fileStorageService;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final EvaluationRepository evaluationRepository;

    public SujetExamenService(SujetExamenRepository sujetExamenRepository, FileStorageService fileStorageService, ApplicationEventPublisher eventPublisher, UserRepository userRepository, EvaluationRepository evaluationRepository) {
        this.sujetExamenRepository = sujetExamenRepository;
        this.fileStorageService = fileStorageService;
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.evaluationRepository = evaluationRepository;
    }

    //creer un sujet d'examen
    public SujetExamenResponseDTO create(SujetExamenRequestDTO dto, MultipartFile file) {
        User enseignant = userRepository.findById(dto.getEnseignantId())
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable"));

        boolean isEnseignant = enseignant.getRoles().stream()
                .anyMatch(role -> "ENSEIGNANT".equals(role.getName()));

        if (!isEnseignant) {
            throw new RuntimeException("L'utilisateur sélectionné n'a pas le rôle ENSEIGNANT");
        }

        Evaluation evaluation = evaluationRepository.findById(dto.getEvaluationId())
                .orElseThrow(() -> new RuntimeException("Évaluation introuvable"));

        SujetExamen sujet = SujetExamenMapper.toEntity(dto);
        sujet.setDateCreation(LocalDate.now());
        sujet.setStatut(StatutSujetExamen.BROUILLON);
        sujet.setEnseignant(enseignant);
        sujet.setEvaluation(evaluation);

        if (file != null && !file.isEmpty()) {
            String storedName = fileStorageService.store(file);
            sujet.setFichierPath(storedName);
        }

        return SujetExamenMapper.toDTO(sujetExamenRepository.save(sujet));
    }


    // modifier
    public SujetExamenResponseDTO update(Long id, SujetExamenRequestDTO dto, MultipartFile file) {
        SujetExamen sujet = sujetExamenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sujet introuvable avec l'id : " + id));

        if (sujet.getStatut() == StatutSujetExamen.VALIDE) {
            throw new RuntimeException("Un sujet validé ne peut plus être modifié.");
        }

        sujet.setTitre(dto.getTitre());
        sujet.setDescription(dto.getDescription());
        sujet.setModule(dto.getModule());
        sujet.setPromotion(dto.getPromotion());
        sujet.setFiliere(dto.getFiliere());
        sujet.setNiveau(dto.getNiveau());
        sujet.setSemestre(dto.getSemestre());

        if (dto.getEnseignantId() != null) {
            User enseignant = userRepository.findById(dto.getEnseignantId())
                    .orElseThrow(() -> new RuntimeException("Enseignant introuvable"));

            boolean isEnseignant = enseignant.getRoles().stream()
                    .anyMatch(role -> "ENSEIGNANT".equals(role.getName()));

            if (!isEnseignant) {
                throw new RuntimeException("L'utilisateur sélectionné n'a pas le rôle ENSEIGNANT");
            }

            sujet.setEnseignant(enseignant);
        }

        if (file != null && !file.isEmpty()) {
            String storedName = fileStorageService.store(file);
            sujet.setFichierPath(storedName);
        }

        return SujetExamenMapper.toDTO(sujetExamenRepository.save(sujet));
    }

   //valider un sujet
   @Transactional
   public SujetExamenActionResponseDTO valider(Long id) {
       SujetExamen sujet = sujetExamenRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Sujet introuvable"));

       if (sujet.getStatut() != StatutSujetExamen.BROUILLON) {
           throw new RuntimeException("Le sujet a déjà été traité.");
       }

       sujet.setStatut(StatutSujetExamen.VALIDE);
       SujetExamen saved = sujetExamenRepository.save(sujet);

       eventPublisher.publishEvent(new SujetValideEvent(
               saved.getId(),
               saved.getEnseignant().getEmail()
       ));

       eventPublisher.publishEvent(new AuditActionEvent(
               saved.getEnseignant().getId(),
               TypeAction.VALIDATION_SUJET,
               "Validation du sujet " + saved.getId()
       ));

       return SujetExamenActionMapper.toActionDTO(saved, "Sujet validé avec succès");
   }


    //rejeter un sujet
    @Transactional
    public SujetExamenActionResponseDTO rejeter(Long id, String motif) {
        SujetExamen sujet = sujetExamenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sujet introuvable"));

        if (sujet.getStatut() != StatutSujetExamen.BROUILLON) {
            throw new RuntimeException("Le sujet a déjà été traité.");
        }

        sujet.setStatut(StatutSujetExamen.REJETE);
        SujetExamen saved = sujetExamenRepository.save(sujet);

        eventPublisher.publishEvent(new SujetRejeteEvent(
                saved.getId(),
                saved.getEnseignant().getEmail(),
                motif
        ));

        eventPublisher.publishEvent(new AuditActionEvent(
                saved.getEnseignant().getId(),
                TypeAction.REJET_SUJET,
                "Rejet du sujet " + saved.getId() + " : " + motif
        ));

        return SujetExamenActionMapper.toActionDTO(saved, "Sujet rejeté avec succès");
    }


    //recherche par id
    public SujetExamenResponseDTO getById(Long id) {
        SujetExamen sujet = sujetExamenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sujet introuvable avec l'id : " + id));
        return SujetExamenMapper.toDTO(sujet);
    }

   //liste des sujets
    public List<SujetExamenResponseDTO> getAll() {
        return sujetExamenRepository.findAll()
                .stream()
                .map(SujetExamenMapper::toDTO)
                .collect(Collectors.toList());
    }

//supprimer un sujet
    public void delete(Long id) {
        SujetExamen sujet = sujetExamenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sujet introuvable avec l'id : " + id));

        if (sujet.getStatut() == StatutSujetExamen.VALIDE || sujet.getStatut() == StatutSujetExamen.ARCHIVE) {
            throw new RuntimeException("Un sujet validé ou archivé ne peut pas être supprimé.");
        }

        sujetExamenRepository.delete(sujet);
    }
//archiver un sujet
@Transactional
public SujetExamenActionResponseDTO archive(Long id) {
    SujetExamen sujet = sujetExamenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sujet introuvable"));

    if (sujet.getStatut() == StatutSujetExamen.ARCHIVE) {
        throw new RuntimeException("Le sujet est déjà archivé.");
    }

    if (sujet.getStatut() != StatutSujetExamen.VALIDE) {
        throw new RuntimeException("Seul un sujet validé peut être archivé.");
    }

    sujet.setStatut(StatutSujetExamen.ARCHIVE);
    SujetExamen saved = sujetExamenRepository.save(sujet);

    return SujetExamenActionMapper.toActionDTO(saved, "Sujet archivé avec succès");
}
//recherche par id enseignant
    public List<SujetExamenResponseDTO> getByEnseignantId(Long enseignantId) {
        return sujetExamenRepository.findByEnseignantId(enseignantId)
                .stream()
                .map(SujetExamenMapper::toDTO)
                .toList();
    }
//rechercher par statut
    public List<SujetExamenResponseDTO> getByStatut(StatutSujetExamen statut) {
        return sujetExamenRepository.findByStatut(statut)
                .stream()
                .map(SujetExamenMapper::toDTO)
                .toList();
    }
// recherche par evaluationId
    public List<SujetExamenResponseDTO> getByEvaluationId(Long evaluationId) {
        return sujetExamenRepository.findByEvaluationId(evaluationId)
                .stream()
                .map(SujetExamenMapper::toDTO)
                .toList();
    }




}
