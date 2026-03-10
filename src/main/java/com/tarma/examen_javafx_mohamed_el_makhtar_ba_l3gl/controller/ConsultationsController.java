package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.controller;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Consultation;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Medecin;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Patient;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.RendezVous;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.StatutRendezVous;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.ConsultationService;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.MedecinService;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.PatientService;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.RendezVousService;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.util.PdfOrdonnanceGenerator;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConsultationsController {

    @FXML
    private TableView<Consultation> consultationsTable;
    @FXML
    private TableColumn<Consultation, String> dateColumn;
    @FXML
    private TableColumn<Consultation, String> patientColumn;
    @FXML
    private TableColumn<Consultation, String> medecinColumn;

    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Patient> patientCombo;
    @FXML
    private ComboBox<Medecin> medecinCombo;
    @FXML
    private ComboBox<RendezVous> rendezVousCombo;
    @FXML
    private TextArea diagnosticArea;
    @FXML
    private TextArea observationsArea;
    @FXML
    private TextArea prescriptionArea;

    private EntityManager em;
    private ConsultationService consultationService;
    private PatientService patientService;
    private MedecinService medecinService;
    private RendezVousService rendezVousService;

    private final ObservableList<Consultation> consultations = FXCollections.observableArrayList();

    public void setContext(EntityManager em, ConsultationService consultationService,
                           PatientService patientService, MedecinService medecinService,
                           RendezVousService rendezVousService) {
        this.em = em;
        this.consultationService = consultationService;
        this.patientService = patientService;
        this.medecinService = medecinService;
        this.rendezVousService = rendezVousService;
        loadData();
    }

    @FXML
    private void initialize() {
        dateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        patientColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getPatient().getNom() + " " + data.getValue().getPatient().getPrenom()));
        medecinColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getMedecin().getNom() + " " + data.getValue().getMedecin().getPrenom()));

        consultationsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                fillForm(newV);
            }
        });

        setupPatientCombo(patientCombo);
        setupMedecinCombo(medecinCombo);
        setupRendezVousDisplay();

        consultationsTable.setItems(consultations);
    }

    @FXML
    private void onEnregistrer() {
        if (consultationService == null) {
            showError("Service consultation non initialise.");
            return;
        }
        LocalDate date = datePicker.getValue();
        Patient patient = patientCombo.getValue();
        Medecin medecin = medecinCombo.getValue();
        RendezVous rendezVous = rendezVousCombo.getValue();

        if (patient == null || medecin == null || rendezVous == null) {
            showError("Patient, medecin et rendez-vous sont obligatoires.");
            return;
        }

        Consultation selected = consultationsTable.getSelectionModel().getSelectedItem();
        Consultation consultation = (selected != null) ? selected : new Consultation();
        consultation.setDate(date == null ? LocalDate.now() : date);
        consultation.setPatient(patient);
        consultation.setMedecin(medecin);
        consultation.setRendezVous(rendezVous);
        consultation.setDiagnostic(diagnosticArea.getText());
        consultation.setObservations(observationsArea.getText());
        consultation.setPrescription(prescriptionArea.getText());

        consultationService.enregistrerConsultation(consultation);
        loadConsultations();
        clearForm();
    }

    @FXML
    private void onExporterOrdonnance() {
        Consultation selected = consultationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selectionnez une consultation.");
            return;
        }
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Exporter ordonnance");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        chooser.setInitialFileName("ordonnance_consultation_" + selected.getId() + ".pdf");
        File file = chooser.showSaveDialog(consultationsTable.getScene().getWindow());
        if (file == null) {
            return;
        }
        try {
            PdfOrdonnanceGenerator.generate(selected, file);
        } catch (Exception ex) {
            showError("Erreur lors de la generation du PDF : " + ex.getMessage());
        }
    }

    private void loadData() {
        patientCombo.setItems(FXCollections.observableArrayList(patientService.findAll()));
        medecinCombo.setItems(FXCollections.observableArrayList(medecinService.findAll()));
        loadRendezVous();
        loadConsultations();
    }

    private void loadRendezVous() {
        List<RendezVous> list = rendezVousService.rendezVousDuJour(LocalDate.now());
        list.removeIf(r -> r.getStatut() != StatutRendezVous.PROGRAMME);
        rendezVousCombo.setItems(FXCollections.observableArrayList(list));
    }

    private void loadConsultations() {
        consultations.setAll(consultationService.findAll());
    }

    private void fillForm(Consultation consultation) {
        datePicker.setValue(consultation.getDate());
        patientCombo.setValue(consultation.getPatient());
        medecinCombo.setValue(consultation.getMedecin());
        rendezVousCombo.setValue(consultation.getRendezVous());
        diagnosticArea.setText(consultation.getDiagnostic());
        observationsArea.setText(consultation.getObservations());
        prescriptionArea.setText(consultation.getPrescription());
    }

    private void clearForm() {
        datePicker.setValue(null);
        patientCombo.setValue(null);
        medecinCombo.setValue(null);
        rendezVousCombo.setValue(null);
        diagnosticArea.clear();
        observationsArea.clear();
        prescriptionArea.clear();
        consultationsTable.getSelectionModel().clearSelection();
    }

    private void setupPatientCombo(ComboBox<Patient> combo) {
        combo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom() + " " + item.getPrenom());
                }
            }
        });
        combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom() + " " + item.getPrenom());
                }
            }
        });
    }

    private void setupMedecinCombo(ComboBox<Medecin> combo) {
        combo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Medecin item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom() + " " + item.getPrenom());
                }
            }
        });
        combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Medecin item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom() + " " + item.getPrenom());
                }
            }
        });
    }

    private void setupRendezVousDisplay() {
        rendezVousCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(RendezVous item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String text = item.getDateHeure().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                            + " - " + item.getPatient().getNom() + " " + item.getPatient().getPrenom();
                    setText(text);
                }
            }
        });
        rendezVousCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(RendezVous item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String text = item.getDateHeure().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                            + " - " + item.getPatient().getNom() + " " + item.getPatient().getPrenom();
                    setText(text);
                }
            }
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}