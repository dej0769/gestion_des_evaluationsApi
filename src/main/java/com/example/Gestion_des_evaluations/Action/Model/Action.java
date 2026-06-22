package com.example.Gestion_des_evaluations.Action.Model;


import com.example.Gestion_des_evaluations.User.Model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "actions")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateAction;

    @Enumerated(EnumType.STRING)
    private TypeAction typeAction;

    @Column(length = 1000)
    private String description;

    @ManyToOne
    private User user;
}
