package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Medecin;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.RendezVous;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.StatutRendezVous;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.implementation.RendezVousRepositoryImpl;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.interfaces.IRendezVousRepository;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class RendezVousService {

    private final EntityManager em;
    private final IRendezVousRepository rendezVousRepository;

    public RendezVousService(EntityManager em) {
        this.em = em;
        this.rendezVousRepository = new RendezVousRepositoryImpl(em);
    }

    public RendezVous planifier(RendezVous rendezVous) {
        verifierConflit(rendezVous.getMedecin(), rendezVous.getDateHeure(), rendezVous.getId());
        em.getTransaction().begin();
        if (rendezVous.getStatut() == null) {
            rendezVous.setStatut(StatutRendezVous.PROGRAMME);
        }
        RendezVous saved = rendezVousRepository.save(rendezVous);
        em.getTransaction().commit();
        return saved;
    }

    public void annuler(RendezVous rendezVous) {
        em.getTransaction().begin();
        rendezVous.setStatut(StatutRendezVous.ANNULE);
        rendezVousRepository.save(rendezVous);
        em.getTransaction().commit();
    }

    public List<RendezVous> rendezVousDuJour(LocalDate date) {
        LocalDateTime start = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);
        return rendezVousRepository.findByDate(start, end);
    }

    private void verifierConflit(Medecin medecin, LocalDateTime dateHeure, Long currentId) {
        List<RendezVous> existants = rendezVousRepository.findByMedecinAndDateTime(medecin, dateHeure);
        for (RendezVous r : existants) {
            boolean sameId = currentId != null && currentId.equals(r.getId());
            if (!sameId && r.getStatut() != StatutRendezVous.ANNULE) {
                throw new IllegalStateException("Ce medecin a deja un rendez-vous a ce creneau.");
            }
        }
    }
}