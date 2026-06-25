package com.example.Gestion_des_evaluations.Entity.Notification.Repository;

import com.example.Gestion_des_evaluations.Entity.Notification.Model.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
}