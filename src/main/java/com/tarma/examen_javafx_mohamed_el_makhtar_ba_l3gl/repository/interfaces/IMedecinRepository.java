package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.interfaces;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Medecin;

import java.util.List;

public interface IMedecinRepository {

    Medecin findById(Long id);

    List<Medecin> findAll();
}

