package com.example.Gestion_des_evaluations.Entity.Reclamation.Event;

public class ReclamationCreeeEvent {
    private final Long reclamationId;

    public ReclamationCreeeEvent(Long reclamationId) {
        this.reclamationId = reclamationId;
    }

    public Long getReclamationId() {
        return reclamationId;
    }
}