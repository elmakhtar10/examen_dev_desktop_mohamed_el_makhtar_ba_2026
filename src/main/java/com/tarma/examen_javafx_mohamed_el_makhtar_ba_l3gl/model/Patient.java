package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private LocalDate dateNaissance;

    @Column(nullable = false)
    private String sexe;

    private String telephone;

    private String adresse;

    private String groupeSanguin;

    @Column(length = 2000)
    private String antecedentsMedicaux;

}

