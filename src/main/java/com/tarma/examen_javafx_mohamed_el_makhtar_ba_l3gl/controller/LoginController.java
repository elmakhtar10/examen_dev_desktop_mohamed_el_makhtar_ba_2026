package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.controller;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.SessionUtilisateur;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Utilisateur;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.AuthService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    private Stage stage;
    private AuthService authService;
    private EntityManager em;

    public void setStage(Stage stage) {
        this.stage = stage;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("cliniquePU");
        this.em = emf.createEntityManager();
        this.authService = new AuthService(em);
    }

    @FXML
    private void onLogin(ActionEvent event) {
        String login = loginField.getText();
        String password = passwordField.getText();

        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            showError("Veuillez saisir le login et le mot de passe.");
            return;
        }

        boolean ok = authService.login(login, password);
        if (!ok) {
            showError("Identifiants incorrects.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tarma/examen_javafx_mohamed_el_makhtar_ba_l3gl/views/dashboard-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);

            DashboardController dashboardController = loader.getController();
            Utilisateur u = SessionUtilisateur.getUtilisateurConnecte();
            dashboardController.setContext(stage, em, u);

            stage.setTitle("Gestion Clinique - Tableau de bord");
            stage.setScene(scene);
        } catch (IOException e) {
            showError("Erreur lors du chargement du tableau de bord : " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

