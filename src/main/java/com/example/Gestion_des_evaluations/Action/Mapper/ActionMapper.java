package com.example.Gestion_des_evaluations.Action.Mapper;

import com.example.Gestion_des_evaluations.Action.DTO.ActionResponseDTO;
import com.example.Gestion_des_evaluations.Action.Model.Action;

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
