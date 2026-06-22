package com.example.Gestion_des_evaluations.Programmation.Model;

import com.example.Gestion_des_evaluations.Evaluation.Model.Evaluation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "programmations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Programmation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateExamen;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String salle;
    private String statut;

    @OneToOne
    @JoinColumn(name = "evaluation_id", unique = true)
    private Evaluation evaluation;
}