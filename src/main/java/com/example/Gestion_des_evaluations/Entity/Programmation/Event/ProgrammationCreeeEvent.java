package com.example.Gestion_des_evaluations.Entity.Programmation.Event;

public class ProgrammationCreeeEvent {
    private final Long programmationId;

    public ProgrammationCreeeEvent(Long programmationId) {
        this.programmationId = programmationId;
    }

    public Long getProgrammationId() {
        return programmationId;
    }
}