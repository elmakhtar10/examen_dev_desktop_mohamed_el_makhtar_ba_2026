package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.controller;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Role;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.UtilisateurService;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UsersController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<Role> roleCombo;

    private UtilisateurService utilisateurService;

    public void setContext(EntityManager em) {
        this.utilisateurService = new UtilisateurService(em);
    }

    @FXML
    private void initialize() {
        roleCombo.setItems(FXCollections.observableArrayList(Role.values()));
    }

    @FXML
    private void onCreate() {
        if (utilisateurService == null) {
            showError("Service utilisateur non initialise.");
            return;
        }
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String login = loginField.getText();
        String password = passwordField.getText();
        Role role = roleCombo.getValue();

        if (isBlank(nom) || isBlank(prenom) || isBlank(login) || isBlank(password) || role == null) {
            showError("Tous les champs sont obligatoires.");
            return;
        }

        try {
            utilisateurService.creerUtilisateur(
                    nom.trim(), prenom.trim(), login.trim(), password, role);
            clearForm();
            showInfo("Utilisateur cree avec succes.");
        } catch (IllegalStateException ex) {
            showError(ex.getMessage());
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private void clearForm() {
        nomField.clear();
        prenomField.clear();
        loginField.clear();
        passwordField.clear();
        roleCombo.setValue(null);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}