package com.example.Gestion_des_evaluations.Dashboard.Service;

import com.example.Gestion_des_evaluations.Entity.Copie.Model.StatutCopie;
import com.example.Gestion_des_evaluations.Entity.Copie.Repository.CopieRepository;
import com.example.Gestion_des_evaluations.Dashboard.DTO.DashboardStatsDTO;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Repository.EvaluationRepository;
import com.example.Gestion_des_evaluations.Entity.Programmation.Repository.ProgrammationRepository;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.ReclamationStatut;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Repository.ReclamationRepository;
import com.example.Gestion_des_evaluations.Entity.Sujet.Repository.SujetExamenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProgrammationRepository programmationRepository;
    private final SujetExamenRepository sujetExamenRepository;
    private final ReclamationRepository reclamationRepository;
    private final EvaluationRepository evaluationRepository;
    private final CopieRepository copieRepository;

    public DashboardStatsDTO getStats() {
        DashboardStatsDTO dto = new DashboardStatsDTO();

        dto.setEvaluationsProgrammees(programmationRepository.count());
        dto.setSujetsDeposes(sujetExamenRepository.count());
        dto.setCopiesCorrigees(copieRepository.countByStatut(StatutCopie.CORRIGEE));
        dto.setReclamationsTraitees(reclamationRepository.countByStatut(ReclamationStatut.TRAITEE));

        Map<String, Long> statsParFiliere = evaluationRepository.countEvaluationsByFiliere()
                .stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));

        dto.setStatistiquesParFiliere(statsParFiliere);

        return dto;
    }
}