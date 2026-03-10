package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Patient;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.implementation.PatientRepositoryImpl;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.interfaces.IPatientRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PatientService {

    private final EntityManager em;
    private final IPatientRepository patientRepository;

    public PatientService(EntityManager em) {
        this.em = em;
        this.patientRepository = new PatientRepositoryImpl(em);
    }

    public Patient save(Patient patient) {
        em.getTransaction().begin();
        Patient saved = patientRepository.save(patient);
        em.getTransaction().commit();
        return saved;
    }

    public void delete(Long id) {
        em.getTransaction().begin();
        patientRepository.delete(id);
        em.getTransaction().commit();
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public List<Patient> searchByName(String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        return patientRepository.searchByName(keyword.trim());
    }
}