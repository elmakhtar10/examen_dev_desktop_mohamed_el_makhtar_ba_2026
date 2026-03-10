package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.controller;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Utilisateur;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.ConsultationService;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.FactureService;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.MedecinService;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.PatientService;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.RendezVousService;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    private Stage stage;
    private EntityManager em;
    private Utilisateur utilisateurConnecte;

    private PatientService patientService;
    private MedecinService medecinService;
    private RendezVousService rendezVousService;
    private ConsultationService consultationService;
    private FactureService factureService;

    public void setContext(Stage stage, EntityManager em, Utilisateur utilisateurConnecte) {
        this.stage = stage;
        this.em = em;
        this.utilisateurConnecte = utilisateurConnecte;

        this.patientService = new PatientService(em);
        this.medecinService = new MedecinService(em);
        this.rendezVousService = new RendezVousService(em);
        this.consultationService = new ConsultationService(em);
        this.factureService = new FactureService(em);
    }

    @FXML
    private void onPatients(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/tarma/examen_javafx_mohamed_el_makhtar_ba_l3gl/views/patients-view.fxml"));
            Scene scene = new Scene(loader.load(), 1100, 650);
            PatientsController controller = loader.getController();
            controller.setContext(em, patientService);

            openDialog("Gestion des patients", scene);
        } catch (IOException e) {
            showError("Erreur lors du chargement des patients : " + e.getMessage());
        }
    }

    @FXML
    private void onRendezVous(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/tarma/examen_javafx_mohamed_el_makhtar_ba_l3gl/views/rendezvous-view.fxml"));
            Scene scene = new Scene(loader.load(), 1100, 650);
            RendezVousController controller = loader.getController();
            controller.setContext(em, rendezVousService, patientService, medecinService);

            openDialog("Gestion des rendez-vous", scene);
        } catch (IOException e) {
            showError("Erreur lors du chargement des rendez-vous : " + e.getMessage());
        }
    }

    @FXML
    private void onConsultations(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/tarma/examen_javafx_mohamed_el_makhtar_ba_l3gl/views/consultations-view.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 700);
            ConsultationsController controller = loader.getController();
            controller.setContext(em, consultationService, patientService, medecinService, rendezVousService);

            openDialog("Gestion des consultations", scene);
        } catch (IOException e) {
            showError("Erreur lors du chargement des consultations : " + e.getMessage());
        }
    }

    @FXML
    private void onFactures(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/tarma/examen_javafx_mohamed_el_makhtar_ba_l3gl/views/factures-view.fxml"));
            Scene scene = new Scene(loader.load(), 1100, 650);
            FacturesController controller = loader.getController();
            controller.setContext(em, factureService, consultationService);

            openDialog("Gestion des factures", scene);
        } catch (IOException e) {
            showError("Erreur lors du chargement des factures : " + e.getMessage());
        }
    }

    private void openDialog(String title, Scene scene) {
        Stage dialog = new Stage();
        dialog.initOwner(stage);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle(title);
        dialog.setScene(scene);
        dialog.setResizable(true);
        dialog.show();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}