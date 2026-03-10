package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.SessionUtilisateur;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Utilisateur;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.util.PasswordUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class AuthService {

    private final EntityManager em;

    public AuthService(EntityManager em) {
        this.em = em;
    }

    public boolean login(String login, String motDePasseClair) {
        String hash = motDePasseClair;
//        String hash = PasswordUtils.hashPassword(motDePasseClair);
        TypedQuery<Utilisateur> query = em.createQuery(
                "SELECT u FROM Utilisateur u WHERE u.login = :login AND u.motDePasseHash = :hash",
                Utilisateur.class
        );
        query.setParameter("login", login);
        query.setParameter("hash", hash);

        try {
            Utilisateur utilisateur = query.getSingleResult();
            SessionUtilisateur.setUtilisateurConnecte(utilisateur);
            return true;
        } catch (NoResultException ex) {
            return false;
        }
    }

    public void logout() {
        SessionUtilisateur.deconnecter();
    }
}