package com.example.Gestion_des_evaluations.Entity.Action.Mapper;

import com.example.Gestion_des_evaluations.Entity.Action.DTO.ActionResponseDTO;
import com.example.Gestion_des_evaluations.Entity.Action.Model.Action;

public class ActionMapper {

    public static ActionResponseDTO toDTO(Action action) {
        if (action == null) {
            return null;
        }
        return new ActionResponseDTO(
                action.getId(),
                action.getDateAction(),
                action.getTypeAction(),
                action.getDescription(),
                action.getUser() != null ? action.getUser().getId() : null
        );
    }
}
