package com.gestionretours.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// Gère l'envoi des alertes par mail — utilisé uniquement pour les non-conformités critiques/hautes
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String expediteur;

    @Value("${app.mail.alert-recipient}")
    private String destinataireAlerte;

    public void envoyerAlerteNonConformite(String produit, String gravite, String description) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(expediteur);
            message.setTo(destinataireAlerte);
            message.setSubject("[ALERTE] Non-conformité " + gravite + " — " + produit);
            message.setText(
                "Une non-conformité de niveau " + gravite + " vient d'être signalée.\n\n" +
                "Produit    : " + produit + "\n" +
                "Gravité    : " + gravite + "\n" +
                "Description : " + description + "\n\n" +
                "Connectez-vous à l'application pour traiter cette non-conformité rapidement."
            );
            mailSender.send(message);
        } catch (Exception e) {
            // On ne bloque pas la création de la non-conformité si l'envoi échoue
            log.error("Impossible d'envoyer l'alerte mail pour la non-conformité sur '{}': {}", produit, e.getMessage());
        }
    }
}
