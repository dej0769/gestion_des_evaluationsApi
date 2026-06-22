package com.example.Gestion_des_evaluations.Auth.Config;

import com.example.Gestion_des_evaluations.User.Model.Role;
import com.example.Gestion_des_evaluations.User.Model.User;
import com.example.Gestion_des_evaluations.User.Repository.RoleRepository;
import com.example.Gestion_des_evaluations.User.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createRoleIfNotExists("ETUDIANT");
        createRoleIfNotExists("ENSEIGNANT");
        createRoleIfNotExists("SURVEILLANT");
        createRoleIfNotExists("RESPONSABLE_PEDAGOGIQUE");
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("AGENT_SCOLARITE");

        if (userRepository.findByEmail("admin@systeme.com").isEmpty()) {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Rôle ADMIN introuvable"));

            User admin = new User();
            admin.setEmail("admin@systeme.com");
            admin.setNom("Admin");
            admin.setPrenom("Systeme");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);
        }
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
