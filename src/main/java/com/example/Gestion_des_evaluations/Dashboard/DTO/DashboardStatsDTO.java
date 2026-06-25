package com.example.Gestion_des_evaluations.Dashboard.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


public class DashboardStatsDTO {
    private long evaluationsProgrammees;
    private long sujetsDeposes;
    private long copiesCorrigees;
    private long reclamationsTraitees;
    private Map<String, Long> statistiquesParFiliere;

    public long getEvaluationsProgrammees() {
        return evaluationsProgrammees;
    }

    public void setEvaluationsProgrammees(long evaluationsProgrammees) {
        this.evaluationsProgrammees = evaluationsProgrammees;
    }

    public long getSujetsDeposes() {
        return sujetsDeposes;
    }

    public void setSujetsDeposes(long sujetsDeposes) {
        this.sujetsDeposes = sujetsDeposes;
    }

    public long getCopiesCorrigees() {
        return copiesCorrigees;
    }

    public void setCopiesCorrigees(long copiesCorrigees) {
        this.copiesCorrigees = copiesCorrigees;
    }

    public long getReclamationsTraitees() {
        return reclamationsTraitees;
    }

    public void setReclamationsTraitees(long reclamationsTraitees) {
        this.reclamationsTraitees = reclamationsTraitees;
    }

    public Map<String, Long> getStatistiquesParFiliere() {
        return statistiquesParFiliere;
    }

    public void setStatistiquesParFiliere(Map<String, Long> statistiquesParFiliere) {
        this.statistiquesParFiliere = statistiquesParFiliere;
    }
}