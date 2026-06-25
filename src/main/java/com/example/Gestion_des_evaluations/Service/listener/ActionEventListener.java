package com.example.Gestion_des_evaluations.Service.listener;

import com.example.Gestion_des_evaluations.Entity.Action.Service.ActionService;
import com.example.Gestion_des_evaluations.Entity.Sujet.Event.AuditActionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ActionEventListener {

    private final ActionService actionService;

    @TransactionalEventListener
    public void onActionEvent(AuditActionEvent event) {
        actionService.enregistrerAutomatiquement(event);
    }
}
