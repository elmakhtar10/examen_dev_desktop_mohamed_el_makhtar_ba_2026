package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.implementation;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Medecin;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.interfaces.IMedecinRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class MedecinRepositoryImpl implements IMedecinRepository {

    private final EntityManager em;

    public MedecinRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Medecin findById(Long id) {
        return em.find(Medecin.class, id);
    }

    @Override
    public List<Medecin> findAll() {
        TypedQuery<Medecin> query = em.createQuery("SELECT m FROM Medecin m", Medecin.class);
        return query.getResultList();
    }
}

