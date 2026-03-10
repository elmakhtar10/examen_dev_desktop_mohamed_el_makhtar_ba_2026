package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Consultation;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Facture;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.StatutFacture;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.implementation.FactureRepositoryImpl;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.interfaces.IFactureRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.List;

public class FactureService {

    private final EntityManager em;
    private final IFactureRepository factureRepository;

    public FactureService(EntityManager em) {
        this.em = em;
        this.factureRepository = new FactureRepositoryImpl(em);
    }

    public Facture genererFacture(Consultation consultation, BigDecimal montantTotal,
                                  String modePaiement, StatutFacture statut) {
        em.getTransaction().begin();
        Facture facture = findByConsultation(consultation);
        if (facture == null) {
            facture = new Facture();
            facture.setConsultation(consultation);
        }
        facture.setMontantTotal(montantTotal);
        facture.setModePaiement(modePaiement);
        facture.setStatut(statut == null ? StatutFacture.NON_PAYE : statut);
        Facture saved = factureRepository.save(facture);
        em.getTransaction().commit();
        return saved;
    }

    public List<Facture> findAll() {
        return factureRepository.findAll();
    }

    public Facture findByConsultation(Consultation consultation) {
        TypedQuery<Facture> query = em.createQuery(
                "SELECT f FROM Facture f WHERE f.consultation = :consultation", Facture.class);
        query.setParameter("consultation", consultation);
        List<Facture> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}