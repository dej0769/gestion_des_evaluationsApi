package com.example.Gestion_des_evaluations.Note.Model;

import com.example.Gestion_des_evaluations.Copy.Model.Copie;
import com.example.Gestion_des_evaluations.User.Model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Getter
@Setter
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double valeur;

    @Column(length = 2000)
    private String commentaire;

    @Column(nullable = false)
    private LocalDateTime dateCorrection;

    @Column(nullable = true)
    private Integer nbreTotalEtudiantsComposes;

    @Column(nullable = true)
    private Double nbreEtudiantsAyantLaMoyenne;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "copie_id", nullable = false, unique = true)
    private Copie copie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "correcteur_id", nullable = false)
    private User correcteur;
}