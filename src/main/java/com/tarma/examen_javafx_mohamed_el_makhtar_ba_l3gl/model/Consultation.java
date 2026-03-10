package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 2000)
    private String diagnostic;

    @Column(length = 2000)
    private String observations;

    @Column(length = 2000)
    private String prescription;

    @ManyToOne(optional = false)
    private Patient patient;

    @ManyToOne(optional = false)
    private Medecin medecin;

    @OneToOne(optional = false)
    private RendezVous rendezVous;

}

