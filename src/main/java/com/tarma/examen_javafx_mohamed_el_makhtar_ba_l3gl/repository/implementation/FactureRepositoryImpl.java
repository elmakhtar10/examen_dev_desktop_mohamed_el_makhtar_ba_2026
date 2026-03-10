package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.implementation;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Facture;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.interfaces.IFactureRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class FactureRepositoryImpl implements IFactureRepository {

    private final EntityManager em;

    public FactureRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Facture save(Facture facture) {
        if (facture.getId() == null) {
            em.persist(facture);
            return facture;
        } else {
            return em.merge(facture);
        }
    }

    @Override
    public Facture findById(Long id) {
        return em.find(Facture.class, id);
    }

    @Override
    public List<Facture> findAll() {
        TypedQuery<Facture> query = em.createQuery("SELECT f FROM Facture f", Facture.class);
        return query.getResultList();
    }
}

