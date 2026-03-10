package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.implementation;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Medecin;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.RendezVous;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.interfaces.IRendezVousRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

public class RendezVousRepositoryImpl implements IRendezVousRepository {

    private final EntityManager em;

    public RendezVousRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public RendezVous save(RendezVous rendezVous) {
        if (rendezVous.getId() == null) {
            em.persist(rendezVous);
            return rendezVous;
        } else {
            return em.merge(rendezVous);
        }
    }

    @Override
    public List<RendezVous> findByMedecinAndDateTime(Medecin medecin, LocalDateTime dateHeure) {
        TypedQuery<RendezVous> query = em.createQuery(
                "SELECT r FROM RendezVous r WHERE r.medecin = :medecin AND r.dateHeure = :dateHeure",
                RendezVous.class);
        query.setParameter("medecin", medecin);
        query.setParameter("dateHeure", dateHeure);
        return query.getResultList();
    }

    @Override
    public List<RendezVous> findByDate(LocalDateTime start, LocalDateTime end) {
        TypedQuery<RendezVous> query = em.createQuery(
                "SELECT r FROM RendezVous r WHERE r.dateHeure BETWEEN :start AND :end",
                RendezVous.class);
        query.setParameter("start", start);
        query.setParameter("end", end);
        return query.getResultList();
    }
}

