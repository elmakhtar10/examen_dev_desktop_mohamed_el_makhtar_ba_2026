package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.repository.interfaces;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Medecin;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.RendezVous;

import java.time.LocalDateTime;
import java.util.List;

public interface IRendezVousRepository {

    RendezVous save(RendezVous rendezVous);

    List<RendezVous> findByMedecinAndDateTime(Medecin medecin, LocalDateTime dateHeure);

    List<RendezVous> findByDate(LocalDateTime start, LocalDateTime end);
}

