package com.example.Gestion_des_evaluations.Note.Mapper;

import com.example.Gestion_des_evaluations.Note.DTO.NoteResponseDTO;
import com.example.Gestion_des_evaluations.Note.Model.Note;

public class NoteMapper {

    public static NoteResponseDTO toDTO(Note note) {
        NoteResponseDTO dto = new NoteResponseDTO();
        dto.setId(note.getId());
        dto.setValeur(note.getValeur());
        dto.setCommentaire(note.getCommentaire());
        dto.setDateCorrection(note.getDateCorrection());
        dto.setNbreTotalEtudiantsComposes(note.getNbreTotalEtudiantsComposes());
        dto.setNbreEtudiantsAyantLaMoyenne(note.getNbreEtudiantsAyantLaMoyenne());
        dto.setCopieId(note.getCopie() != null ? note.getCopie().getId() : null);
        dto.setCorrecteurId(note.getCorrecteur() != null ? note.getCorrecteur().getId() : null);
        return dto;
    }
}