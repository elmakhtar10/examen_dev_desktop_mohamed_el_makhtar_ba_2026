package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Consultation;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.RendezVous;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.StatutRendezVous;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.implementation.ConsultationRepositoryImpl;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.interfaces.IConsultationRepository;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class ConsultationService {

    private final EntityManager em;
    private final IConsultationRepository consultationRepository;

    public ConsultationService(EntityManager em) {
        this.em = em;
        this.consultationRepository = new ConsultationRepositoryImpl(em);
    }

    public Consultation enregistrerConsultation(Consultation consultation) {
        em.getTransaction().begin();
        if (consultation.getDate() == null) {
            consultation.setDate(LocalDate.now());
        }
        RendezVous rdv = consultation.getRendezVous();
        if (rdv != null) {
            rdv.setStatut(StatutRendezVous.TERMINE);
            em.merge(rdv);
        }
        Consultation saved = consultationRepository.save(consultation);
        em.getTransaction().commit();
        return saved;
    }

    public List<Consultation> findAll() {
        return consultationRepository.findAll();
    }
}