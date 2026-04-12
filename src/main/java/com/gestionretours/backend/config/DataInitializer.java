package com.gestionretours.backend.config;

import com.gestionretours.backend.model.entity.*;
import com.gestionretours.backend.model.enums.*;
import com.gestionretours.backend.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Data initializer — seeds default users and realistic company data on first startup.
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
    private final EntityManager entityManager;

    @Value("${app.data.force-reinit:false}")
    private boolean forceReinit;

    @Override
    @Transactional
    public void run(String... args) {
        if (forceReinit) {
            log.warn("Force re-initialization requested — clearing all data...");
            historiqueRetourRepository.deleteAllInBatch();
            nonConformiteRepository.deleteAllInBatch();
            retourProduitRepository.deleteAllInBatch();
            utilisateurRepository.deleteAllInBatch();
            entityManager.flush();
            entityManager.clear();
        } else if (utilisateurRepository.count() > 0) {
            log.info("Database already initialized, skipping seed data");
            return;
        }

        log.info("Initializing database with realistic company data...");

        // ── Users / Utilisateurs ──────────────────────────────────────
        Utilisateur admin = utilisateurRepository.save(Utilisateur.builder()
                .nom("Admin Système").email("admin@retours.com")
                .motDePasse(passwordEncoder.encode("Admin123!"))
                .role(RoleUtilisateur.ROLE_ADMIN).build());

        Utilisateur qualite1 = utilisateurRepository.save(Utilisateur.builder()
                .nom("Responsable Qualité").email("qualite@retours.com")
                .motDePasse(passwordEncoder.encode("Qualite123!"))
                .role(RoleUtilisateur.ROLE_QUALITE).build());

        Utilisateur qualite2 = utilisateurRepository.save(Utilisateur.builder()
                .nom("Nadia Mansouri").email("n.mansouri@qualite.com")
                .motDePasse(passwordEncoder.encode("Qualite123!"))
                .role(RoleUtilisateur.ROLE_QUALITE).build());

        Utilisateur emp1 = utilisateurRepository.save(Utilisateur.builder()
                .nom("Employé Test").email("employe@retours.com")
                .motDePasse(passwordEncoder.encode("Employe123!"))
                .role(RoleUtilisateur.ROLE_EMPLOYE).build());

        Utilisateur emp2 = utilisateurRepository.save(Utilisateur.builder()
                .nom("Karim Bouzidi").email("k.bouzidi@entreprise.com")
                .motDePasse(passwordEncoder.encode("Employe123!"))
                .role(RoleUtilisateur.ROLE_EMPLOYE).build());

        Utilisateur emp3 = utilisateurRepository.save(Utilisateur.builder()
                .nom("Salma Trabelsi").email("s.trabelsi@entreprise.com")
                .motDePasse(passwordEncoder.encode("Employe123!"))
                .role(RoleUtilisateur.ROLE_EMPLOYE).build());

        Utilisateur emp4 = utilisateurRepository.save(Utilisateur.builder()
                .nom("Youssef Chaabane").email("y.chaabane@entreprise.com")
                .motDePasse(passwordEncoder.encode("Employe123!"))
                .role(RoleUtilisateur.ROLE_EMPLOYE).build());

        Utilisateur emp5 = utilisateurRepository.save(Utilisateur.builder()
                .nom("Ines Ben Ali").email("i.benali@entreprise.com")
                .motDePasse(passwordEncoder.encode("Employe123!"))
                .role(RoleUtilisateur.ROLE_EMPLOYE).build());

        // ── Product Returns / Retours Produits ────────────────────────
        // Défaut de fabrication (8)
        RetourProduit r1 = save(retourProduitRepository, "Moteur A200", "Société Belhaj", "Défaut de fabrication",
                "Vibrations anormales détectées lors du démarrage", EtatTraitement.EN_ATTENTE, emp1);
        RetourProduit r2 = save(retourProduitRepository, "Moteur A200", "STEG Tunis", "Défaut de fabrication",
                "Surchauffe du bobinage après 30 min de fonctionnement", EtatTraitement.EN_COURS, emp2);
        RetourProduit r3 = save(retourProduitRepository, "Compresseur C400", "Tunisie Télécom", "Défaut de fabrication",
                "Joint d'étanchéité défaillant dès la première utilisation", EtatTraitement.TRAITE, emp3);
        RetourProduit r4 = save(retourProduitRepository, "Réducteur R150", "Ciment de Bizerte", "Défaut de fabrication",
                "Roulement interne grippé — bruit anormal", EtatTraitement.EN_ATTENTE, emp4);
        RetourProduit r5 = save(retourProduitRepository, "Pompe P100", "Industrie Sud", "Défaut de fabrication",
                "Carter fissuré visible à la réception", EtatTraitement.VALIDE, emp1);
        RetourProduit r6 = save(retourProduitRepository, "Vérin V250", "Groupe Chimique Gabès", "Défaut de fabrication",
                "Tige du piston tordue — problème assemblage", EtatTraitement.EN_COURS, emp5);
        RetourProduit r7 = save(retourProduitRepository, "Moteur A200", "Société Belhaj", "Défaut de fabrication",
                "Connecteur électrique mal serti — faux contacts", EtatTraitement.REJETE, emp2);
        RetourProduit r8 = save(retourProduitRepository, "Alternateur AL80", "SNCFT", "Défaut de fabrication",
                "Défaut isolement bobinage — court-circuit intermittent", EtatTraitement.TRAITE, emp3);

        // Produit endommagé (6)
        RetourProduit r9 = save(retourProduitRepository, "Valve HV3", "EPC Sfax", "Produit endommagé",
                "Corps de vanne fissuré pendant transport routier", EtatTraitement.EN_ATTENTE, emp1);
        RetourProduit r10 = save(retourProduitRepository, "Capteur T8", "Agro Nabeul", "Produit endommagé",
                "Écran LCD cassé — emballage insuffisant", EtatTraitement.EN_COURS, emp4);
        RetourProduit r11 = save(retourProduitRepository, "Filtre F55", "SONEDE", "Produit endommagé",
                "Cartouche filtrante déformée à la livraison", EtatTraitement.TRAITE, emp2);
        RetourProduit r12 = save(retourProduitRepository, "Débitmètre D300", "ONAS Sousse", "Produit endommagé",
                "Connectique arrachée lors de la manutention", EtatTraitement.EN_ATTENTE, emp5);
        RetourProduit r13 = save(retourProduitRepository, "Pressostat PS20", "Carthage Cement", "Produit endommagé",
                "Boîtier plastique fendu — choc visible", EtatTraitement.VALIDE, emp3);
        RetourProduit r14 = save(retourProduitRepository, "Pompe P100", "Délice Danone", "Produit endommagé",
                "Pales de l'impulseur tordues", EtatTraitement.EN_COURS, emp1);

        // Non conforme à la commande (5)
        RetourProduit r15 = save(retourProduitRepository, "Capteur T8", "EPC Sfax", "Non conforme à la commande",
                "Référence reçue T8-12V au lieu de T8-24V", EtatTraitement.REJETE, qualite1);
        RetourProduit r16 = save(retourProduitRepository, "Vanne V100", "STEG Tunis", "Non conforme à la commande",
                "Diamètre DN80 reçu au lieu de DN100 commandé", EtatTraitement.EN_ATTENTE, emp2);
        RetourProduit r17 = save(retourProduitRepository, "Câble CB500", "Tunisair Technics", "Non conforme à la commande",
                "Section 2.5mm² livrée au lieu de 4mm²", EtatTraitement.TRAITE, emp4);
        RetourProduit r18 = save(retourProduitRepository, "Roulement RLT40", "Poulina Group", "Non conforme à la commande",
                "Roulement 6205 reçu à la place du 6208", EtatTraitement.EN_COURS, emp5);
        RetourProduit r19 = save(retourProduitRepository, "Moteur A200", "Société Belhaj", "Non conforme à la commande",
                "Moteur monophasé livré au lieu de triphasé", EtatTraitement.EN_ATTENTE, emp1);

        // Problème de qualité (5)
        RetourProduit r20 = save(retourProduitRepository, "Peinture PX200", "Ménara Holding", "Problème de qualité",
                "Couleur non conforme RAL — différence visible", EtatTraitement.EN_ATTENTE, emp3);
        RetourProduit r21 = save(retourProduitRepository, "Huile HY46", "Star Montage", "Problème de qualité",
                "Viscosité hors normes ISO — analyse labo confirmée", EtatTraitement.VALIDE, emp2);
        RetourProduit r22 = save(retourProduitRepository, "Filtre F55", "SONEDE", "Problème de qualité",
                "Média filtrant de mauvaise qualité — colmatage prématuré", EtatTraitement.EN_COURS, emp4);
        RetourProduit r23 = save(retourProduitRepository, "Joint JT10", "Ciment de Bizerte", "Problème de qualité",
                "Caoutchouc friable — vulcanisation incomplète", EtatTraitement.TRAITE, emp5);
        RetourProduit r24 = save(retourProduitRepository, "Tuyau TX300", "STIR Bizerte", "Problème de qualité",
                "Paroi plus fine que spécifiée — risque de rupture", EtatTraitement.EN_ATTENTE, emp1);

        // Erreur d'expédition (4)
        RetourProduit r25 = save(retourProduitRepository, "Compresseur C400", "Tunisie Télécom", "Erreur d'expédition",
                "Colis destiné à un autre client — erreur logistique", EtatTraitement.TRAITE, emp2);
        RetourProduit r26 = save(retourProduitRepository, "Valve HV3", "Agro Nabeul", "Erreur d'expédition",
                "Quantité reçue 5 au lieu de 10 commandées", EtatTraitement.EN_ATTENTE, emp3);
        RetourProduit r27 = save(retourProduitRepository, "Alternateur AL80", "SNCFT", "Erreur d'expédition",
                "Mauvais site de livraison — Tunis au lieu de Sousse", EtatTraitement.EN_COURS, emp4);
        RetourProduit r28 = save(retourProduitRepository, "Débitmètre D300", "ONAS Sousse", "Erreur d'expédition",
                "Double livraison — facturé en double", EtatTraitement.VALIDE, emp5);

        // Délai dépassé (3)
        RetourProduit r29 = save(retourProduitRepository, "Réducteur R150", "Poulina Group", "Délai dépassé",
                "Livraison avec 3 semaines de retard — arrêt production", EtatTraitement.TRAITE, emp1);
        RetourProduit r30 = save(retourProduitRepository, "Pompe P100", "Délice Danone", "Délai dépassé",
                "Délai contractuel 15j — livré à J+32", EtatTraitement.EN_ATTENTE, emp2);
        RetourProduit r31 = save(retourProduitRepository, "Câble CB500", "STEG Tunis", "Délai dépassé",
                "Retard critique — pénalités de retard applicables", EtatTraitement.EN_COURS, emp3);

        // Insatisfaction client (3)
        RetourProduit r32 = save(retourProduitRepository, "Pressostat PS20", "Star Montage", "Insatisfaction client",
                "Performances en deçà des attentes — imprécis", EtatTraitement.EN_ATTENTE, emp4);
        RetourProduit r33 = save(retourProduitRepository, "Vérin V250", "Carthage Cement", "Insatisfaction client",
                "Bruit excessif en fonctionnement — gêne opérateur", EtatTraitement.REJETE, emp5);
        RetourProduit r34 = save(retourProduitRepository, "Huile HY46", "Ménara Holding", "Insatisfaction client",
                "Durée de vie réduite par rapport à la fiche technique", EtatTraitement.VALIDE, emp1);

        // Autre (3)
        RetourProduit r35 = save(retourProduitRepository, "Roulement RLT40", "SNCFT", "Autre",
                "Rappel fournisseur — lot potentiellement affecté", EtatTraitement.EN_COURS, emp2);
        RetourProduit r36 = save(retourProduitRepository, "Tuyau TX300", "ONAS Sousse", "Autre",
                "Certificat matière manquant — blocage douane", EtatTraitement.EN_ATTENTE, emp3);
        RetourProduit r37 = save(retourProduitRepository, "Joint JT10", "Agro Nabeul", "Autre",
                "Changement de fournisseur — retour stock ancien", EtatTraitement.TRAITE, emp4);

        List<RetourProduit> allRetours = List.of(r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12,r13,r14,r15,r16,r17,r18,r19,r20,r21,r22,r23,r24,r25,r26,r27,r28,r29,r30,r31,r32,r33,r34,r35,r36,r37);

        // ── Non-Conformities / Non-Conformités ────────────────────────
        saveNC(nonConformiteRepository, "Moteur A200", Gravite.HAUTE, "Vibrations hors tolérance ISO 10816", r1);
        saveNC(nonConformiteRepository, "Moteur A200", Gravite.CRITIQUE, "Risque de surchauffe — danger incendie", r2);
        saveNC(nonConformiteRepository, "Compresseur C400", Gravite.MOYENNE, "Joint non conforme — fuite légère", r3);
        saveNC(nonConformiteRepository, "Réducteur R150", Gravite.HAUTE, "Roulement grippé — usure prématurée", r4);
        saveNC(nonConformiteRepository, "Pompe P100", Gravite.CRITIQUE, "Fissure carter — risque de fuite huile", r5);
        saveNC(nonConformiteRepository, "Vérin V250", Gravite.HAUTE, "Tige tordue — défaut assemblage usine", r6);
        saveNC(nonConformiteRepository, "Alternateur AL80", Gravite.CRITIQUE, "Court-circuit isolement — sécurité électrique", r8);
        saveNC(nonConformiteRepository, "Valve HV3", Gravite.MOYENNE, "Corps fissuré — contrainte mécanique transport", r9);
        saveNC(nonConformiteRepository, "Filtre F55", Gravite.FAIBLE, "Déformation légère cartouche", r11);
        saveNC(nonConformiteRepository, "Tuyau TX300", Gravite.HAUTE, "Épaisseur paroi 2.1mm au lieu de 3mm — risque rupture", r24);
        saveNC(nonConformiteRepository, "Peinture PX200", Gravite.MOYENNE, "Écart colorimétrique ΔE > 3", r20);
        saveNC(nonConformiteRepository, "Huile HY46", Gravite.FAIBLE, "Viscosité limite basse — encore acceptable", r21);
        saveNC(nonConformiteRepository, "Joint JT10", Gravite.HAUTE, "Vulcanisation incomplète — lot entier suspect", r23);

        // ── History / Historique ──────────────────────────────────────
        for (RetourProduit r : allRetours) {
            historiqueRetourRepository.save(HistoriqueRetour.builder()
                    .retour(r).action("Retour créé").employe(r.getUtilisateur())
                    .nouvelEtat(EtatTraitement.EN_ATTENTE).build());
        }

        // State transitions for EN_COURS items
        for (RetourProduit r : List.of(r2, r6, r10, r14, r18, r22, r27, r31, r35)) {
            historiqueRetourRepository.save(HistoriqueRetour.builder()
                    .retour(r).action("Pris en charge par l'équipe qualité").employe(qualite1)
                    .ancienEtat(EtatTraitement.EN_ATTENTE).nouvelEtat(EtatTraitement.EN_COURS).build());
        }
        // TRAITE transitions
        for (RetourProduit r : List.of(r3, r8, r11, r17, r23, r25, r29, r37)) {
            historiqueRetourRepository.save(HistoriqueRetour.builder()
                    .retour(r).action("Pris en charge par l'équipe qualité").employe(qualite2)
                    .ancienEtat(EtatTraitement.EN_ATTENTE).nouvelEtat(EtatTraitement.EN_COURS).build());
            historiqueRetourRepository.save(HistoriqueRetour.builder()
                    .retour(r).action("Traitement terminé").employe(qualite1)
                    .ancienEtat(EtatTraitement.EN_COURS).nouvelEtat(EtatTraitement.TRAITE).build());
        }
        // VALIDE transitions
        for (RetourProduit r : List.of(r5, r13, r21, r28, r34)) {
            historiqueRetourRepository.save(HistoriqueRetour.builder()
                    .retour(r).action("Pris en charge").employe(qualite1)
                    .ancienEtat(EtatTraitement.EN_ATTENTE).nouvelEtat(EtatTraitement.EN_COURS).build());
            historiqueRetourRepository.save(HistoriqueRetour.builder()
                    .retour(r).action("Retour validé — remboursement ou remplacement approuvé").employe(qualite2)
                    .ancienEtat(EtatTraitement.EN_COURS).nouvelEtat(EtatTraitement.VALIDE).build());
        }
        // REJETE transitions
        for (RetourProduit r : List.of(r7, r15, r33)) {
            historiqueRetourRepository.save(HistoriqueRetour.builder()
                    .retour(r).action("Retour rejeté — non éligible au traitement").employe(admin)
                    .ancienEtat(EtatTraitement.EN_ATTENTE).nouvelEtat(EtatTraitement.REJETE).build());
        }

        log.info("✅ Database initialized with {} returns, {} NC, {} users",
                allRetours.size(), nonConformiteRepository.count(), utilisateurRepository.count());
    }

    private RetourProduit save(RetourProduitRepository repo, String produit, String client,
                                String raison, String description, EtatTraitement etat, Utilisateur user) {
        return repo.save(RetourProduit.builder()
                .produit(produit).client(client).raison(raison)
                .description(description).etatTraitement(etat).utilisateur(user).build());
    }

    private void saveNC(NonConformiteRepository repo, String produit, Gravite gravite,
                         String description, RetourProduit retour) {
        repo.save(NonConformite.builder()
                .produit(produit).gravite(gravite).description(description).retour(retour).build());
    }
}
