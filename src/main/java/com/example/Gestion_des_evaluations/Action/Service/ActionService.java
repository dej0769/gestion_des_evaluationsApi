package com.example.Gestion_des_evaluations.Action.Service;
import com.example.Gestion_des_evaluations.Action.DTO.ActionResponseDTO;
import com.example.Gestion_des_evaluations.Action.Mapper.ActionMapper;
import com.example.Gestion_des_evaluations.Action.Model.Action;
import com.example.Gestion_des_evaluations.Action.Repository.ActionRepository;
import com.example.Gestion_des_evaluations.Subject.Event.AuditActionEvent;
import com.example.Gestion_des_evaluations.User.Model.User;
import com.example.Gestion_des_evaluations.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionService {

    private final ActionRepository actionRepository;
    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void enregistrerAutomatiquement(AuditActionEvent event) {
        User utilisateur = userRepository.findById(event.userId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Action action = new Action();
        action.setDateAction(LocalDateTime.now());
        action.setTypeAction(event.typeAction());
        action.setDescription(event.description());
        action.setUser(utilisateur);

        actionRepository.save(action);
    }

    public List<ActionResponseDTO> liste() {
        return actionRepository.findAll().stream()
                .map(ActionMapper::toDTO)
                .toList();
    }
}