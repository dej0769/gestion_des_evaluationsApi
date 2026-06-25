package com.example.Gestion_des_evaluations.Entity.Reclamation.Event;

public class ReclamationTraiteeEvent {
    private final Long reclamationId;
    private final String statut;

    public ReclamationTraiteeEvent(Long reclamationId, String statut) {
        this.reclamationId = reclamationId;
        this.statut = statut;
    }

    public Long getReclamationId() {
        return reclamationId;
    }

    public String getStatut() {
        return statut;
    }
}