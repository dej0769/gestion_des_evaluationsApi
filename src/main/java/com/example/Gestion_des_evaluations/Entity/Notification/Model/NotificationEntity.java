package com.example.Gestion_des_evaluations.Entity.Notification.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destinataire;

    private String message;

    private LocalDateTime dateCreation;

    private boolean lu = false;
}
