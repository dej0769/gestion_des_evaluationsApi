package com.example.Gestion_des_evaluations.Entity.Incident.Model;

import com.example.Gestion_des_evaluations.Entity.Evaluation.Model.Evaluation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
@Getter
@Setter
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateSignalement;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private IncidentType type;

    @Enumerated(EnumType.STRING)
    private IncidentStatut statut;

    @ManyToOne
    private Evaluation evaluation;
}