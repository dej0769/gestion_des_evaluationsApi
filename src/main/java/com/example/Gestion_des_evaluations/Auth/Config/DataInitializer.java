package com.example.Gestion_des_evaluations.Auth.Config;

import com.example.Gestion_des_evaluations.Auth.Model.Role;
import com.example.Gestion_des_evaluations.Auth.Repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        createRoleIfNotExists("ETUDIANT");
        createRoleIfNotExists("ENSEIGNANT");
        createRoleIfNotExists("SURVEILLANT");
        createRoleIfNotExists("RESPONSABLE_PEDAGOGIQUE");
        createRoleIfNotExists("ADMIN");
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
