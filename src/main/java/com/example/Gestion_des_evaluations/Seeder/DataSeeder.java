package com.example.Gestion_des_evaluations.Seeder;

import com.example.Gestion_des_evaluations.Entity.Copie.Model.Copie;
import com.example.Gestion_des_evaluations.Entity.Copie.Model.StatutCopie;
import com.example.Gestion_des_evaluations.Entity.Copie.Repository.CopieRepository;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Model.Evaluation;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Model.StatutEvaluation;
import com.example.Gestion_des_evaluations.Entity.Evaluation.Repository.EvaluationRepository;
import com.example.Gestion_des_evaluations.Entity.Incident.Model.Incident;
import com.example.Gestion_des_evaluations.Entity.Incident.Model.IncidentStatut;
import com.example.Gestion_des_evaluations.Entity.Incident.Model.IncidentType;
import com.example.Gestion_des_evaluations.Entity.Incident.Repository.IncidentRepository;
import com.example.Gestion_des_evaluations.Entity.Note.Model.Note;
import com.example.Gestion_des_evaluations.Entity.Note.Repository.NoteRepository;
import com.example.Gestion_des_evaluations.Entity.Programmation.Model.Programmation;
import com.example.Gestion_des_evaluations.Entity.Programmation.Repository.ProgrammationRepository;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.Reclamation;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.ReclamationStatut;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.SessionReclamation;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Model.SessionStatut;
import com.example.Gestion_des_evaluations.Entity.Reclamation.Repository.ReclamationRepository;
import com.example.Gestion_des_evaluations.Entity.Sujet.Model.StatutSujetExamen;
import com.example.Gestion_des_evaluations.Entity.Sujet.Model.SujetExamen;
import com.example.Gestion_des_evaluations.Entity.Sujet.Repository.SujetExamenRepository;
import com.example.Gestion_des_evaluations.Entity.User.Model.Role;
import com.example.Gestion_des_evaluations.Entity.User.Model.User;
import com.example.Gestion_des_evaluations.Entity.User.Repository.RoleRepository;
import com.example.Gestion_des_evaluations.Entity.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EvaluationRepository evaluationRepository;
    private final SujetExamenRepository sujetExamenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReclamationRepository reclamationRepository;
    private final NoteRepository noteRepository;
    private final ProgrammationRepository programmationRepository;
    private final CopieRepository copieRepository;
    private final IncidentRepository incidentRepository;

    @Override
    public void run(ApplicationArguments args) {
        seedRoles();
        seedUsers();
        seedEvaluations();
        seedSujets();
        seedReclamations();
        seedProgrammations();
        seedNotes();
        seedCopies();
    }

    private void seedRoles() {
        if (roleRepository.count() > 0) {
            return;
        }

        Role etudiant = new Role();
        etudiant.setName("ETUDIANT");

        Role enseignant = new Role();
        enseignant.setName("ENSEIGNANT");

        Role surveillant = new Role();
        surveillant.setName("SURVEILLANT");

        Role responsablePedagogique = new Role();
        responsablePedagogique.setName("RESPONSABLE_PEDAGOGIQUE");

        Role admin = new Role();
        admin.setName("ADMIN");

        Role agentScolarite = new Role();
        agentScolarite.setName("AGENT_SCOLARITE");

        roleRepository.saveAll(List.of(
                etudiant,
                enseignant,
                surveillant,
                responsablePedagogique,
                admin,
                agentScolarite
        ));
    }

    private void seedUsers() {
        if (userRepository.count() > 0) {
            return;
        }

        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
        Role enseignantRole = roleRepository.findByName("ENSEIGNANT").orElseThrow();
        Role etudiantRole = roleRepository.findByName("ETUDIANT").orElseThrow();

        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setMatricule("ADM001");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setNom("Barry");
        admin.setPrenom("Ibrahima");
        admin.setTelephone("600000003");
        admin.setActive(true);
        admin.setRoles(new HashSet<>(Set.of(adminRole)));

        User enseignant = new User();
        enseignant.setEmail("enseignant@gmail.com");
        enseignant.setMatricule("ENS001");
        enseignant.setPassword(passwordEncoder.encode("123456"));
        enseignant.setNom("Diallo");
        enseignant.setPrenom("Moussa");
        enseignant.setTelephone("600000001");
        enseignant.setActive(true);
        enseignant.setRoles(new HashSet<>(Set.of(enseignantRole)));

        User etudiant = new User();
        etudiant.setEmail("etudiant@gmail.com");
        etudiant.setMatricule("ETU001");
        etudiant.setPassword(passwordEncoder.encode("123456"));
        etudiant.setNom("Camara");
        etudiant.setPrenom("Aminata");
        etudiant.setTelephone("600000002");
        etudiant.setActive(true);
        etudiant.setRoles(new HashSet<>(Set.of(etudiantRole)));

        userRepository.saveAll(List.of(admin, enseignant, etudiant));
    }

    private void seedEvaluations() {
        if (evaluationRepository.count() > 0) {
            return;
        }

        User enseignant = userRepository.findByEmail("enseignant@gmail.com").orElseThrow();

        Evaluation evaluation = new Evaluation();
        evaluation.setDateEvaluation(LocalDate.now().plusDays(7));
        evaluation.setHeureDebut(LocalTime.of(8, 0));
        evaluation.setHeureFin(LocalTime.of(10, 0));
        evaluation.setSalle("Salle A1");
        evaluation.setModule("Recherche Operationnel");
        evaluation.setPromotion("BAC 2024");
        evaluation.setFiliere("Informatique");
        evaluation.setSession("Normale");
        evaluation.setNiveau("Licence");
        evaluation.setSemestre("S4");
        evaluation.setStatut(StatutEvaluation.PROGRAMMEE);
        evaluation.setEnseignant(enseignant);

        evaluationRepository.save(evaluation);
    }

    private void seedSujets() {
        if (sujetExamenRepository.count() > 0) {
            return;
        }

        User enseignant = userRepository.findByEmail("enseignant@gmail.com").orElseThrow();
        Evaluation evaluation = evaluationRepository.findAll().get(0);

        SujetExamen sujet = new SujetExamen();
        sujet.setTitre("Sujet de Mathématiques");
        sujet.setDescription("Sujet de contrôle continu sur les équations.");
        sujet.setModule("Mathématiques");
        sujet.setPromotion("L2");
        sujet.setFiliere("Informatique");
        sujet.setNiveau("Licence");
        sujet.setSemestre("S4");
        sujet.setDateCreation(LocalDate.now());
        sujet.setFichierPath(null);
        sujet.setStatut(StatutSujetExamen.BROUILLON);
        sujet.setEnseignant(enseignant);
        sujet.setEvaluation(evaluation);

        sujetExamenRepository.save(sujet);
    }

    private void seedReclamations() {
        if (reclamationRepository.count() > 0) {
            return;
        }

        if (noteRepository.count() == 0) {
            return;
        }

        Note note = noteRepository.findAll().get(0);

        SessionReclamation session = new SessionReclamation();
        session.setStatut(SessionStatut.OUVERTE);

        Reclamation reclamation = new Reclamation();
        reclamation.setDateCreation(LocalDate.now());
        reclamation.setDescription("L'étudiant demande une vérification de sa note.");
        reclamation.setFichier(null);
        reclamation.setObjet("Réclamation de note");
        reclamation.setTypeDocument("NOTE");
        reclamation.setStatut(ReclamationStatut.EN_COURS);
        reclamation.setSessionReclamation(session);
        reclamation.setNote(note);

        reclamationRepository.save(reclamation);
    }

    private void seedProgrammations() {
        if (programmationRepository.count() > 0) {
            return;
        }

        if (evaluationRepository.count() == 0) {
            return;
        }

        Evaluation evaluation = evaluationRepository.findAll().get(0);

        Programmation programmation = new Programmation();
        programmation.setDateExamen(LocalDate.now().plusDays(7));
        programmation.setHeureDebut(LocalTime.of(8, 0));
        programmation.setHeureFin(LocalTime.of(10, 0));
        programmation.setSalle("Salle A1");
        programmation.setStatut("PLANIFIEE");
        programmation.setEvaluation(evaluation);

        programmationRepository.save(programmation);
    }

    private void seedNotes() {
        if (noteRepository.count() > 0) {
            return;
        }

        if (copieRepository.count() == 0 || userRepository.count() == 0) {
            return;
        }

        Copie copie = copieRepository.findAll().get(0);
        User correcteur = userRepository.findByEmail("enseignant@gmail.com").orElseThrow();

        Note note = new Note();
        note.setValeur(14.5);
        note.setCommentaire("Bon travail.");
        note.setDateCorrection(LocalDateTime.now());
        note.setNbreTotalEtudiantsComposes(1);
        note.setNbreEtudiantsAyantLaMoyenne(1.0);
        note.setCopie(copie);
        note.setCorrecteur(correcteur);

        noteRepository.save(note);
    }

    private void seedIncidentsIfMissing() {
        if (incidentRepository.count() > 0) {
            return;
        }

        if (evaluationRepository.count() == 0) {
            return;
        }

        Evaluation evaluation = evaluationRepository.findAll().get(0);

        Incident incident = new Incident();
        incident.setDateSignalement(LocalDateTime.now());
        incident.setDescription("Incident de tricherie détecté pendant l'examen.");
        incident.setType(IncidentType.TRICHERIE);
        incident.setStatut(IncidentStatut.SIGNALE);
        incident.setEvaluation(evaluation);

        incidentRepository.save(incident);
    }

    private void seedCopies() {
        if (copieRepository.count() > 0) {
            return;
        }

        if (sujetExamenRepository.count() == 0 || userRepository.count() == 0) {
            return;
        }

        SujetExamen sujet = sujetExamenRepository.findAll().get(0);
        User etudiant = userRepository.findByEmail("etudiant@gmail.com").orElseThrow();

        Copie copie = new Copie();
        copie.setFichierPath("/files/copies/copie1.pdf");
        copie.setDateDepot(LocalDateTime.now().minusDays(1));
        copie.setDateRetour(null);
        copie.setDateRetrait(null);
        copie.setNbreTotalCopie(1);
        copie.setStatut(StatutCopie.DEPOSEE);
        copie.setSujetExamen(sujet);
        copie.setEtudiant(etudiant);

        copieRepository.save(copie);
    }
}