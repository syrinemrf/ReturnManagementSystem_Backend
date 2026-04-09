package com.gestionretours.backend.config;

import com.gestionretours.backend.model.entity.*;
import com.gestionretours.backend.model.enums.*;
import com.gestionretours.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data initializer — seeds default users and sample data on first startup.
 * Initialiseur de données — insère les utilisateurs par défaut et les données exemples au premier démarrage.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final RetourProduitRepository retourProduitRepository;
    private final NonConformiteRepository nonConformiteRepository;
    private final HistoriqueRetourRepository historiqueRetourRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (utilisateurRepository.count() > 0) {
            log.info("Database already initialized, skipping seed data / Base de données déjà initialisée, données ignorées");
            return;
        }

        log.info("Initializing database with sample data... / Initialisation de la base de données avec des données exemples...");

        // ── Users / Utilisateurs ──────────────────────────────────────
        Utilisateur admin = utilisateurRepository.save(Utilisateur.builder()
                .nom("Admin Système")
                .email("admin@retours.com")
                .motDePasse(passwordEncoder.encode("Admin123!"))
                .role(RoleUtilisateur.ROLE_ADMIN)
                .build());

        Utilisateur qualite = utilisateurRepository.save(Utilisateur.builder()
                .nom("Responsable Qualité")
                .email("qualite@retours.com")
                .motDePasse(passwordEncoder.encode("Qualite123!"))
                .role(RoleUtilisateur.ROLE_QUALITE)
                .build());

        Utilisateur employe = utilisateurRepository.save(Utilisateur.builder()
                .nom("Employé Test")
                .email("employe@retours.com")
                .motDePasse(passwordEncoder.encode("Employe123!"))
                .role(RoleUtilisateur.ROLE_EMPLOYE)
                .build());

        // ── Product Returns / Retours Produits ────────────────────────
        RetourProduit retour1 = retourProduitRepository.save(RetourProduit.builder()
                .produit("Moteur A200")
                .client("Société Belhaj")
                .raison("Défaut fabrication")
                .description("Vibrations anormales détectées lors du démarrage / Abnormal vibrations detected at startup")
                .etatTraitement(EtatTraitement.EN_ATTENTE)
                .utilisateur(employe)
                .build());

        RetourProduit retour2 = retourProduitRepository.save(RetourProduit.builder()
                .produit("Valve HV3")
                .client("STEG Tunis")
                .raison("Non conforme spec")
                .description("Pression de fonctionnement hors tolérance / Operating pressure out of tolerance")
                .etatTraitement(EtatTraitement.EN_COURS)
                .utilisateur(employe)
                .build());

        RetourProduit retour3 = retourProduitRepository.save(RetourProduit.builder()
                .produit("Pompe P100")
                .client("Industrie Sud")
                .raison("Livraison endommagée")
                .description("Carter fissuré lors du transport / Cracked casing during transport")
                .etatTraitement(EtatTraitement.TRAITE)
                .utilisateur(qualite)
                .build());

        RetourProduit retour4 = retourProduitRepository.save(RetourProduit.builder()
                .produit("Capteur T8")
                .client("EPC Sfax")
                .raison("Mauvaise commande")
                .description("Référence incorrecte commandée / Incorrect reference ordered")
                .etatTraitement(EtatTraitement.REJETE)
                .utilisateur(qualite)
                .build());

        RetourProduit retour5 = retourProduitRepository.save(RetourProduit.builder()
                .produit("Filtre F55")
                .client("Agro Nabeul")
                .raison("Défaut emballage")
                .description("Emballage déchiré, produit exposé à l'humidité / Torn packaging, product exposed to moisture")
                .etatTraitement(EtatTraitement.VALIDE)
                .utilisateur(employe)
                .build());

        // ── Non-Conformities / Non-Conformités ────────────────────────
        nonConformiteRepository.save(NonConformite.builder()
                .produit("Moteur A200")
                .gravite(Gravite.HAUTE)
                .description("Fissure détectée sur carter / Crack detected on casing")
                .retour(retour1)
                .build());

        nonConformiteRepository.save(NonConformite.builder()
                .produit("Valve HV3")
                .gravite(Gravite.CRITIQUE)
                .description("Fuite hydraulique sous pression / Hydraulic leak under pressure")
                .retour(retour2)
                .build());

        nonConformiteRepository.save(NonConformite.builder()
                .produit("Filtre F55")
                .gravite(Gravite.FAIBLE)
                .description("Légère déformation emballage / Slight packaging deformation")
                .retour(retour5)
                .build());

        // ── History / Historique ──────────────────────────────────────
        historiqueRetourRepository.save(HistoriqueRetour.builder()
                .retour(retour1)
                .action("Retour créé / Return created")
                .employe(employe)
                .nouvelEtat(EtatTraitement.EN_ATTENTE)
                .build());

        historiqueRetourRepository.save(HistoriqueRetour.builder()
                .retour(retour2)
                .action("Retour créé / Return created")
                .employe(employe)
                .nouvelEtat(EtatTraitement.EN_ATTENTE)
                .build());

        historiqueRetourRepository.save(HistoriqueRetour.builder()
                .retour(retour2)
                .action("Pris en charge par équipe qualité / Taken over by quality team")
                .employe(qualite)
                .ancienEtat(EtatTraitement.EN_ATTENTE)
                .nouvelEtat(EtatTraitement.EN_COURS)
                .build());

        historiqueRetourRepository.save(HistoriqueRetour.builder()
                .retour(retour3)
                .action("Retour créé / Return created")
                .employe(qualite)
                .nouvelEtat(EtatTraitement.EN_ATTENTE)
                .build());

        historiqueRetourRepository.save(HistoriqueRetour.builder()
                .retour(retour3)
                .action("Traitement terminé, remplacement effectué / Treatment completed, replacement done")
                .employe(qualite)
                .ancienEtat(EtatTraitement.EN_COURS)
                .nouvelEtat(EtatTraitement.TRAITE)
                .build());

        historiqueRetourRepository.save(HistoriqueRetour.builder()
                .retour(retour4)
                .action("Retour rejeté — erreur de commande client / Return rejected — customer order error")
                .employe(admin)
                .ancienEtat(EtatTraitement.EN_ATTENTE)
                .nouvelEtat(EtatTraitement.REJETE)
                .build());

        historiqueRetourRepository.save(HistoriqueRetour.builder()
                .retour(retour5)
                .action("Retour validé, remboursement approuvé / Return validated, refund approved")
                .employe(qualite)
                .ancienEtat(EtatTraitement.EN_COURS)
                .nouvelEtat(EtatTraitement.VALIDE)
                .build());

        System.out.println("✅ Database initialized with sample data / Base de données initialisée avec des données exemples");
        log.info("✅ Database initialized with sample data");
    }
}
