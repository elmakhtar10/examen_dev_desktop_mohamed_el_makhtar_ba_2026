package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.interfaces;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Facture;

import java.util.List;

public interface IFactureRepository {

    Facture save(Facture facture);

    Facture findById(Long id);

    List<Facture> findAll();
}

