package com.example.Gestion_des_evaluations.Service.listener;

import com.example.Gestion_des_evaluations.Entity.Notification.Service.NotificationService;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Event.ReclamationCreeeEvent;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Event.ReclamationTraiteeEvent;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.Reclamation;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Repository.ReclamationRepository;
import com.example.Gestion_des_evaluations.Entity.User.Model.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ReclamationNotificationListener {

    private final ReclamationRepository reclamationRepository;
    private final NotificationService notificationService;

    public ReclamationNotificationListener(ReclamationRepository reclamationRepository,
                                           NotificationService notificationService) {
        this.reclamationRepository = reclamationRepository;
        this.notificationService = notificationService;
    }

    @TransactionalEventListener
    public void onReclamationCreee(ReclamationCreeeEvent event) {
        Reclamation reclamation = reclamationRepository.findById(event.getReclamationId())
                .orElseThrow(() -> new RuntimeException("Réclamation introuvable"));

        User enseignant = reclamation.getSujetExamen().getEnseignant();
        User etudiant = reclamation.getEtudiant();

        notificationService.notifier(enseignant,
                "Une nouvelle réclamation a été soumise par un étudiant.");
    }

    @TransactionalEventListener
    public void onReclamationTraitee(ReclamationTraiteeEvent event) {
        Reclamation reclamation = reclamationRepository.findById(event.getReclamationId())
                .orElseThrow(() -> new RuntimeException("Réclamation introuvable"));

        User etudiant = reclamation.getEtudiant();

        if ("TRAITEE".equals(event.getStatut())) {
            notificationService.notifier(etudiant,
                    "Votre réclamation a été prise en compte.");
        }

        if ("REJETEE".equals(event.getStatut())) {
            notificationService.notifier(etudiant,
                    "Votre réclamation a été rejetée.");
        }
    }
}
