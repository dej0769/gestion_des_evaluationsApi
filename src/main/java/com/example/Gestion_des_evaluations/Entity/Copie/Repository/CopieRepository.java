package com.example.Gestion_des_evaluations.Entity.Copie.Repository;

import com.example.Gestion_des_evaluations.Entity.Copie.Model.Copie;
import com.example.Gestion_des_evaluations.Entity.Copie.Model.StatutCopie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CopieRepository extends JpaRepository<Copie, Long> {

    List<Copie> findBySujetExamenId(Long sujetExamenId);

    List<Copie> findByEtudiantId(Long etudiantId);

    List<Copie> findByStatut(StatutCopie statut);

    List<Copie> findByEtudiantIdAndStatut(Long etudiantId, StatutCopie statut);

    List<Copie> findBySujetExamenIdAndStatut(Long sujetExamenId, StatutCopie statut);

}