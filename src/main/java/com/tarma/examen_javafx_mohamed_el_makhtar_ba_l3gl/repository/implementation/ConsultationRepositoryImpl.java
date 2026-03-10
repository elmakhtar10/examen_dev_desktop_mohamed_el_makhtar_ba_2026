package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.implementation;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Consultation;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.interfaces.IConsultationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ConsultationRepositoryImpl implements IConsultationRepository {

    private final EntityManager em;

    public ConsultationRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Consultation save(Consultation consultation) {
        if (consultation.getId() == null) {
            em.persist(consultation);
            return consultation;
        } else {
            return em.merge(consultation);
        }
    }

    @Override
    public Consultation findById(Long id) {
        return em.find(Consultation.class, id);
    }

    @Override
    public List<Consultation> findAll() {
        TypedQuery<Consultation> query = em.createQuery(
                "SELECT c FROM Consultation c JOIN c.medecin m JOIN c.patient p ORDER BY c.date DESC", Consultation.class);
        return query.getResultList();
    }
}