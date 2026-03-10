package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.implementation;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Patient;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.interfaces.IPatientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PatientRepositoryImpl implements IPatientRepository {

    private final EntityManager em;

    public PatientRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Patient save(Patient patient) {
        if (patient.getId() == null) {
            em.persist(patient);
            return patient;
        } else {
            return em.merge(patient);
        }
    }

    @Override
    public void delete(Long id) {
        Patient p = em.find(Patient.class, id);
        if (p != null) {
            em.remove(p);
        }
    }

    @Override
    public Patient findById(Long id) {
        return em.find(Patient.class, id);
    }

    @Override
    public List<Patient> findAll() {
        TypedQuery<Patient> query = em.createQuery("SELECT p FROM Patient p", Patient.class);
        return query.getResultList();
    }

    @Override
    public List<Patient> searchByName(String name) {
        TypedQuery<Patient> query = em.createQuery(
                "SELECT p FROM Patient p WHERE LOWER(p.nom) LIKE :kw OR LOWER(p.prenom) LIKE :kw",
                Patient.class);
        query.setParameter("kw", "%" + name.toLowerCase() + "%");
        return query.getResultList();
    }
}

