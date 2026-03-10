package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Utilisateur;
import lombok.Data;

@Data
public final class SessionUtilisateur {

    private static Utilisateur utilisateurConnecte;

    private SessionUtilisateur() {
    }

    public static void deconnecter() {
        utilisateurConnecte = null;
    }
}

