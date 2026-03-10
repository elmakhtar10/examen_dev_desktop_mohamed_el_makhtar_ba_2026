package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Medecin;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.implementation.MedecinRepositoryImpl;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.interfaces.IMedecinRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class MedecinService {

    private final IMedecinRepository medecinRepository;

    public MedecinService(EntityManager em) {
        this.medecinRepository = new MedecinRepositoryImpl(em);
    }

    public List<Medecin> findAll() {
        return medecinRepository.findAll();
    }
}