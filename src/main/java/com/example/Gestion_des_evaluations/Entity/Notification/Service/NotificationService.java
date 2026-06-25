package com.example.Gestion_des_evaluations.Entity.Notification.Service;

import com.example.Gestion_des_evaluations.Entity.Notification.Model.NotificationEntity;
import com.example.Gestion_des_evaluations.Entity.Notification.Repository.NotificationRepository;
import com.example.Gestion_des_evaluations.Entity.User.Model.User;
import com.example.Gestion_des_evaluations.Service.Mail.EmailService;
import org.springframework.stereotype.Service;

import javax.management.Notification;
import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public NotificationService(NotificationRepository notificationRepository,
                               EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
    }

    public void notifier(User user, String message) {
        NotificationEntity notification = new NotificationEntity();
        notification.setDestinataire(user.getEmail());
        notification.setMessage(message);
        notification.setDateCreation(LocalDateTime.now());
        notification.setLu(false);
        notificationRepository.save(notification);

        emailService.sendEmail(user.getEmail(), "Notification système", message);
    }
}