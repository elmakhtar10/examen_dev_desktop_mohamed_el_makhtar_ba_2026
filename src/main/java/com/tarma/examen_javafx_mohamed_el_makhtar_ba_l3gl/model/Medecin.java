package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MEDECIN")
public class Medecin extends Utilisateur {
}

