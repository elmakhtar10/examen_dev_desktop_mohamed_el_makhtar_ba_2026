package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Utilisateur;

public final class SessionUtilisateur {

    private static Utilisateur utilisateurConnecte;

    private SessionUtilisateur() {
    }

    public static Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public static void setUtilisateurConnecte(Utilisateur utilisateur) {
        utilisateurConnecte = utilisateur;
    }

    public static void deconnecter() {
        utilisateurConnecte = null;
    }
}

