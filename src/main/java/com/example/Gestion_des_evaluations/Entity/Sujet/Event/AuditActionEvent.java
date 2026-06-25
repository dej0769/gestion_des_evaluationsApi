package com.example.Gestion_des_evaluations.Entity.Sujet.Event;

import com.example.Gestion_des_evaluations.Entity.Action.Model.TypeAction;

public record AuditActionEvent(
        Long userId,
        TypeAction typeAction,
        String description
) {}
