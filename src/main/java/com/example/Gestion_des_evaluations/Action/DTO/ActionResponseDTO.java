package com.example.Gestion_des_evaluations.Action.DTO;

import com.example.Gestion_des_evaluations.Action.Model.TypeAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionResponseDTO {
    private Long id;
    private LocalDateTime dateAction;
    private TypeAction typeAction;
    private String description;
    private Long userId;
}