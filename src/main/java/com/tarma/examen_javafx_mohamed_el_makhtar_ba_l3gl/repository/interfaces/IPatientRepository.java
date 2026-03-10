package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.interfaces;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Patient;

import java.util.List;

public interface IPatientRepository {

    Patient save(Patient patient);

    void delete(Long id);

    Patient findById(Long id);

    List<Patient> findAll();

    List<Patient> searchByName(String keyword);
}

