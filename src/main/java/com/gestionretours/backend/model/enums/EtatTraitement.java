package com.gestionretours.backend.model.enums;

// Les différents états possibles d'un retour — suit le cycle de vie du traitement
public enum EtatTraitement {
    EN_ATTENTE,
    EN_COURS,
    VALIDE,
    REJETE,
    TRAITE
}
