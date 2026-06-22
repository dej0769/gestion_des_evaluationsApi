package com.example.Gestion_des_evaluations.Subject.Event;

import com.example.Gestion_des_evaluations.Action.Model.TypeAction;

public record AuditActionEvent(
        Long userId,
        TypeAction typeAction,
        String description
) {}
