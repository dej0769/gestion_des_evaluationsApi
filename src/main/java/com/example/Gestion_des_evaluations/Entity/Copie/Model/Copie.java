package com.example.Gestion_des_evaluations.Entity.Copie.Model;

import com.example.Gestion_des_evaluations.Entity.Evaluation.Model.Evaluation;
import com.example.Gestion_des_evaluations.Entity.Sujet.Model.SujetExamen;
import com.example.Gestion_des_evaluations.Entity.User.Model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "copies")
@Getter
@Setter
public class Copie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fichierPath;

    @Column(nullable = false)
    private LocalDateTime dateDepot;

    @Column(nullable = true)
    private LocalDateTime dateRetour;

    @Column(nullable = true)
    private LocalDateTime dateRetrait;

    @Column(nullable = false)
    private int nbreTotalCopie = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCopie statut = StatutCopie.DEPOSEE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sujet_examen_id", nullable = false)
    private SujetExamen sujetExamen;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private User etudiant;

    @ManyToOne
    @JoinColumn(name = "correcteur_id")
    private User correcteur;

    @ManyToOne
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;
}