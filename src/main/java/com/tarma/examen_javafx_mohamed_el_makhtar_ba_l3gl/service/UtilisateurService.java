package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Administrateur;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Medecin;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Receptionniste;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Role;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Utilisateur;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.util.PasswordUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class UtilisateurService {

    private final EntityManager em;

    public UtilisateurService(EntityManager em) {
        this.em = em;
    }

    public Utilisateur creerUtilisateur(String nom, String prenom, String login,
                                       String motDePasseClair, Role role) {
        if (loginExiste(login)) {
            throw new IllegalStateException("Ce login existe deja.");
        }

        Utilisateur utilisateur = buildUtilisateur(role);
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setLogin(login);
        utilisateur.setRole(role);
        utilisateur.setMotDePasseHash(PasswordUtils.hashPassword(motDePasseClair));

        em.getTransaction().begin();
        em.persist(utilisateur);
        em.getTransaction().commit();

        return utilisateur;
    }

    public boolean loginExiste(String login) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(u) FROM Utilisateur u WHERE u.login = :login",
                Long.class);
        query.setParameter("login", login);
        Long count = query.getSingleResult();
        return count != null && count > 0;
    }

    private Utilisateur buildUtilisateur(Role role) {
        if (role == Role.ADMIN) {
            return new Administrateur();
        }
        if (role == Role.MEDECIN) {
            return new Medecin();
        }
        return new Receptionniste();
    }
}