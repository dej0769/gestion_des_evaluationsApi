package com.example.Gestion_des_evaluations.Entity.Evaluation.Model;

import com.example.Gestion_des_evaluations.Entity.Programmation.Model.Programmation;
import com.example.Gestion_des_evaluations.Entity.User.Model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;



@Setter
@Getter
@Entity
@Table( name = "evaluations")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Date prévue pour l'évaluation
    @Column(nullable = false)
    private LocalDate dateEvaluation;


    // Heure de début de l'évaluation
    @Column(nullable = false)
    private LocalTime heureDebut;


    // Heure de fin de l'évaluation
    @Column(nullable = false)
    private LocalTime heureFin;


    // Salle affectée à l'évaluation
    @Column(nullable = false)
    private String salle;


    // Module ou matière concernée par l'évaluation
    @Column(nullable = false)
    private String module;
    @Column(nullable = false)
    private String promotion;
    @Column(nullable = false)
    private String filiere;

    @Column(nullable = false)
    private String session;

    @Column(nullable = false)
    private String niveau;
    @Column(nullable = false)
    private String semestre;

    // Statut de l'évaluation : programmée, validée, annulée, etc.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutEvaluation statut = StatutEvaluation.PROGRAMMEE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programmationId")
    private Programmation programmation;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignantId")
    private User enseignant;
    public Evaluation() {
    }


}