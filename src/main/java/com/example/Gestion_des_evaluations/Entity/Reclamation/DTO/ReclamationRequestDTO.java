package com.example.Gestion_des_evaluations.Entity.Reclamation.DTO;

import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.ReclamationStatut;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReclamationRequestDTO {
    private LocalDate dateCreation;
    private String description;
    private MultipartFile fichier;
    private String objet;
    private String typeDocument;
    private ReclamationStatut statut;
    private Long noteId;
    private Long sessionId;
}