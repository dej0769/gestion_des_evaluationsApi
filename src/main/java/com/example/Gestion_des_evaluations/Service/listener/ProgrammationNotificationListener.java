package com.example.Gestion_des_evaluations.Service.listener;

import com.example.Gestion_des_evaluations.Entity.Notification.Service.NotificationService;
import com.example.Gestion_des_evaluations.Entity.Programmation.Event.ProgrammationCreeeEvent;
import com.example.Gestion_des_evaluations.Entity.Programmation.Model.Programmation;
import com.example.Gestion_des_evaluations.Entity.Programmation.Repository.ProgrammationRepository;
import com.example.Gestion_des_evaluations.Entity.User.Model.User;
import com.example.Gestion_des_evaluations.Entity.User.Repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
public class ProgrammationNotificationListener {

    private final ProgrammationRepository programmationRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public ProgrammationNotificationListener(ProgrammationRepository programmationRepository,
                                             NotificationService notificationService,
                                             UserRepository userRepository) {
        this.programmationRepository = programmationRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @TransactionalEventListener
    public void onProgrammationCreee(ProgrammationCreeeEvent event) {
        Programmation programmation = programmationRepository.findById(event.getProgrammationId())
                .orElseThrow(() -> new RuntimeException("Programmation introuvable"));

        if (programmation.getEvaluation() == null) {
            return;
        }

        User enseignant = programmation.getEvaluation().getEnseignant();

        List<User> etudiants = userRepository.findAllByRoles_Name("ETUDIANT");

        notificationService.notifier(enseignant, "Une programmation a été créée pour votre évaluation.");

        for (User etudiant : etudiants) {
            notificationService.notifier(etudiant, "Une nouvelle programmation d'examen est disponible.");
        }
    }
}

