package com.example.Gestion_des_evaluations.Subject.Service;

import com.example.Gestion_des_evaluations.Action.Model.TypeAction;
import com.example.Gestion_des_evaluations.Subject.DTO.SujetExamenRequestDTO;
import com.example.Gestion_des_evaluations.Subject.DTO.SujetExamenResponseDTO;
import com.example.Gestion_des_evaluations.Subject.Event.AuditActionEvent;
import com.example.Gestion_des_evaluations.Subject.Event.SujetRejeteEvent;
import com.example.Gestion_des_evaluations.Subject.Event.SujetValideEvent;
import com.example.Gestion_des_evaluations.Subject.Mapper.SujetExamenMapper;
import com.example.Gestion_des_evaluations.Subject.Model.StatutSujetExamen;
import com.example.Gestion_des_evaluations.Subject.Model.SujetExamen;
import com.example.Gestion_des_evaluations.Subject.Repository.SujetExamenRepository;
import com.example.Gestion_des_evaluations.User.Model.User;
import com.example.Gestion_des_evaluations.User.Repository.UserRepository;
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

    public SujetExamenService(SujetExamenRepository sujetExamenRepository, FileStorageService fileStorageService, ApplicationEventPublisher eventPublisher, UserRepository userRepository) {
        this.sujetExamenRepository = sujetExamenRepository;
        this.fileStorageService = fileStorageService;
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
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

        SujetExamen sujet = SujetExamenMapper.toEntity(dto);
        sujet.setDateCreation(LocalDate.now());
        sujet.setStatut(StatutSujetExamen.BROUILLON);
        sujet.setEnseignant(enseignant);

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
   public SujetExamen valider(Long id) {
       SujetExamen sujet = sujetExamenRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Sujet introuvable"));

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

       return saved;
   }


    //rejeter un sujet
    @Transactional
    public SujetExamen rejeter(Long id, String motif) {
        SujetExamen sujet = sujetExamenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sujet introuvable"));

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

        return saved;
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


    public void delete(Long id) {
        SujetExamen sujet = sujetExamenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sujet introuvable avec l'id : " + id));

        if (sujet.getStatut() == StatutSujetExamen.VALIDE || sujet.getStatut() == StatutSujetExamen.ARCHIVE) {
            throw new RuntimeException("Un sujet validé ou archivé ne peut pas être supprimé.");
        }

        sujetExamenRepository.delete(sujet);
    }

    public SujetExamenResponseDTO archive(Long id) {
        SujetExamen sujet = sujetExamenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sujet introuvable avec l'id : " + id));

        if (sujet.getStatut() != StatutSujetExamen.VALIDE) {
            throw new RuntimeException("Seul un sujet validé peut être archivé.");
        }

        sujet.setStatut(StatutSujetExamen.ARCHIVE);
        return SujetExamenMapper.toDTO(sujetExamenRepository.save(sujet));
    }

    public List<SujetExamenResponseDTO> getByEnseignantId(Long enseignantId) {
        return sujetExamenRepository.findByEnseignantId(enseignantId)
                .stream()
                .map(SujetExamenMapper::toDTO)
                .toList();
    }

    public List<SujetExamenResponseDTO> getByStatut(StatutSujetExamen statut) {
        return sujetExamenRepository.findByStatut(statut)
                .stream()
                .map(SujetExamenMapper::toDTO)
                .toList();
    }

    public List<SujetExamenResponseDTO> getByEvaluationId(Long evaluationId) {
        return sujetExamenRepository.findByEvaluationId(evaluationId)
                .stream()
                .map(SujetExamenMapper::toDTO)
                .toList();
    }

    public List<SujetExamenResponseDTO> getByModule(String module) {
        return sujetExamenRepository.findByModule(module)
                .stream()
                .map(SujetExamenMapper::toDTO)
                .toList();
    }


}
