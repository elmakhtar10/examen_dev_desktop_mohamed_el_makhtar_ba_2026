package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.controller;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Patient;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.PatientService;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class PatientsController {

    @FXML
    private TableView<Patient> patientsTable;
    @FXML
    private TableColumn<Patient, String> nomColumn;
    @FXML
    private TableColumn<Patient, String> prenomColumn;
    @FXML
    private TableColumn<Patient, LocalDate> dateNaissanceColumn;

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private DatePicker dateNaissancePicker;
    @FXML
    private ComboBox<String> sexeCombo;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField adresseField;
    @FXML
    private TextField groupeSanguinField;
    @FXML
    private TextArea antecedentsArea;
    @FXML
    private TextField searchField;

    private EntityManager em;
    private PatientService patientService;

    private final ObservableList<Patient> patients = FXCollections.observableArrayList();

    public void setContext(EntityManager em, PatientService patientService) {
        this.em = em;
        this.patientService = patientService;
        loadPatients();
    }

    @FXML
    private void initialize() {
        nomColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
        prenomColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPrenom()));
        dateNaissanceColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDateNaissance()));

        sexeCombo.setItems(FXCollections.observableArrayList("M", "F"));

        patientsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                fillForm(newV);
            }
        });

        patientsTable.setItems(patients);
    }

    @FXML
    private void onSave() {
        if (patientService == null) {
            showError("Service patient non initialise.");
            return;
        }
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        LocalDate dateN = dateNaissancePicker.getValue();
        String sexe = sexeCombo.getValue();

        if (nom == null || nom.isBlank() || prenom == null || prenom.isBlank()
                || dateN == null || sexe == null || sexe.isBlank()) {
            showError("Nom, prenom, date de naissance et sexe sont obligatoires.");
            return;
        }

        Patient selected = patientsTable.getSelectionModel().getSelectedItem();
        Patient patient = (selected != null) ? selected : new Patient();
        patient.setNom(nom.trim());
        patient.setPrenom(prenom.trim());
        patient.setDateNaissance(dateN);
        patient.setSexe(sexe);
        patient.setTelephone(telephoneField.getText());
        patient.setAdresse(adresseField.getText());
        patient.setGroupeSanguin(groupeSanguinField.getText());
        patient.setAntecedentsMedicaux(antecedentsArea.getText());

        patientService.save(patient);
        loadPatients();
        clearForm();
    }

    @FXML
    private void onDelete() {
        Patient selected = patientsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selectionnez un patient a supprimer.");
            return;
        }
        patientService.delete(selected.getId());
        loadPatients();
        clearForm();
    }

    @FXML
    private void onSearch() {
        if (patientService == null) {
            return;
        }
        String keyword = searchField.getText();
        List<Patient> results;
        if (keyword == null || keyword.isBlank()) {
            results = patientService.findAll();
        } else {
            results = patientService.searchByName(keyword);
        }
        patients.setAll(results);
    }

    private void loadPatients() {
        if (patientService == null) {
            return;
        }
        patients.setAll(patientService.findAll());
    }

    private void fillForm(Patient patient) {
        nomField.setText(patient.getNom());
        prenomField.setText(patient.getPrenom());
        dateNaissancePicker.setValue(patient.getDateNaissance());
        sexeCombo.setValue(patient.getSexe());
        telephoneField.setText(patient.getTelephone());
        adresseField.setText(patient.getAdresse());
        groupeSanguinField.setText(patient.getGroupeSanguin());
        antecedentsArea.setText(patient.getAntecedentsMedicaux());
    }

    private void clearForm() {
        nomField.clear();
        prenomField.clear();
        dateNaissancePicker.setValue(null);
        sexeCombo.setValue(null);
        telephoneField.clear();
        adresseField.clear();
        groupeSanguinField.clear();
        antecedentsArea.clear();
        patientsTable.getSelectionModel().clearSelection();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}