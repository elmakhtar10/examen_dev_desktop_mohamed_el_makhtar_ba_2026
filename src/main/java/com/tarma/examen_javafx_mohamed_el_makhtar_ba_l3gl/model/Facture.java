package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal montantTotal;

    @Column(nullable = false)
    private String modePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutFacture statut;

    @OneToOne(optional = false)
    private Consultation consultation;
}

