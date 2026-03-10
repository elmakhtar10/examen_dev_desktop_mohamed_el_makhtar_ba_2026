package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.interfaces;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Consultation;

import java.util.List;

public interface IConsultationRepository {

    Consultation save(Consultation consultation);

    Consultation findById(Long id);

    List<Consultation> findAll();
}