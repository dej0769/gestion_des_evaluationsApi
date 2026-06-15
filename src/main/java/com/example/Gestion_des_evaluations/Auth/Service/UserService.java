package com.example.Gestion_des_evaluations.Auth.Service;


import com.example.Gestion_des_evaluations.Auth.Model.User;
import com.example.Gestion_des_evaluations.Auth.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }
}
