package com.example.Gestion_des_evaluations.Entity.Sujet.Model;

import com.example.Gestion_des_evaluations.Entity.Evaluation.Model.Evaluation;
import com.example.Gestion_des_evaluations.Entity.User.Model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "sujets_examen")
public class SujetExamen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private String module;

    @Column(nullable = false)
    private String promotion;

    @Column(nullable = false)
    private String filiere;

    @Column(nullable = false)
    private String niveau;

    @Column(nullable = false)
    private String semestre;

    @Column(nullable = false)
    private LocalDate dateCreation;

    private String fichierPath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutSujetExamen statut = StatutSujetExamen.BROUILLON;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignant_id", nullable = false)
    private User enseignant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluationId", nullable = false)
    private Evaluation evaluation;
}