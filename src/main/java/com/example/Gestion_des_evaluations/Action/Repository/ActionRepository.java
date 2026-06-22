package com.example.Gestion_des_evaluations.Action.Repository;

import com.example.Gestion_des_evaluations.Action.Model.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, Long> {
}
