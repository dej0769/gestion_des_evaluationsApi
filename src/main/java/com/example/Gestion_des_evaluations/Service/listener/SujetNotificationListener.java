package com.example.Gestion_des_evaluations.Service.listener;


import com.example.Gestion_des_evaluations.Service.Mail.EmailService;
import com.example.Gestion_des_evaluations.Entity.Sujet.Event.SujetRejeteEvent;
import com.example.Gestion_des_evaluations.Entity.Sujet.Event.SujetValideEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SujetNotificationListener {

    private final EmailService emailService;

    public SujetNotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSujetValide(SujetValideEvent event) {
        emailService.sendEmail(
                event.email(),
                "Sujet validé",
                "Bonjour,\n\nVotre sujet d'examen a été validé.\n\nCordialement."
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSujetRejete(SujetRejeteEvent event) {
        emailService.sendEmail(
                event.email(),
                "Sujet rejeté",
                "Bonjour,\n\nVotre sujet d'examen a été rejeté.\nMotif : "
                        + event.motif()
                        + "\n\nCordialement."
        );
    }
}
