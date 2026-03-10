package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.controller;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Medecin;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Patient;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.RendezVous;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.StatutRendezVous;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.MedecinService;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.PatientService;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.RendezVousService;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RendezVousController {

    @FXML
    private TableView<RendezVous> rendezVousTable;
    @FXML
    private TableColumn<RendezVous, String> patientColumn;
    @FXML
    private TableColumn<RendezVous, String> medecinColumn;
    @FXML
    private TableColumn<RendezVous, String> dateHeureColumn;
    @FXML
    private TableColumn<RendezVous, String> statutColumn;

    @FXML
    private DatePicker filtreDatePicker;
    @FXML
    private ComboBox<Medecin> filtreMedecinCombo;

    @FXML
    private DatePicker datePicker;
    @FXML
    private Spinner<Integer> heureSpinner;
    @FXML
    private Spinner<Integer> minuteSpinner;
    @FXML
    private ComboBox<Patient> patientCombo;
    @FXML
    private ComboBox<Medecin> medecinCombo;
    @FXML
    private ComboBox<StatutRendezVous> statutCombo;

    private EntityManager em;
    private RendezVousService rendezVousService;
    private PatientService patientService;
    private MedecinService medecinService;

    private final ObservableList<RendezVous> rendezVousList = FXCollections.observableArrayList();

    public void setContext(EntityManager em, RendezVousService rendezVousService,
                           PatientService patientService, MedecinService medecinService) {
        this.em = em;
        this.rendezVousService = rendezVousService;
        this.patientService = patientService;
        this.medecinService = medecinService;
        loadData();
    }

    @FXML
    private void initialize() {
        patientColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getPatient().getNom() + " " + data.getValue().getPatient().getPrenom()));
        medecinColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getMedecin().getNom() + " " + data.getValue().getMedecin().getPrenom()));
        dateHeureColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDateHeure().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        statutColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getStatut().name()));

        heureSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 5));

        statutCombo.setItems(FXCollections.observableArrayList(StatutRendezVous.values()));

        rendezVousTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                fillForm(newV);
            }
        });

        setupPatientCombo(patientCombo);
        setupMedecinCombo(medecinCombo);
        setupMedecinCombo(filtreMedecinCombo);

        rendezVousTable.setItems(rendezVousList);
        filtreDatePicker.setValue(LocalDate.now());
    }

    @FXML
    private void onFiltrer() {
        if (rendezVousService == null) {
            return;
        }
        LocalDate date = filtreDatePicker.getValue();
        if (date == null) {
            date = LocalDate.now();
        }
        List<RendezVous> list = rendezVousService.rendezVousDuJour(date);
        Medecin filtreMed = filtreMedecinCombo.getValue();
        if (filtreMed != null) {
            list.removeIf(r -> !filtreMed.equals(r.getMedecin()));
        }
        rendezVousList.setAll(list);
    }

    @FXML
    private void onPlanifier() {
        if (rendezVousService == null) {
            showError("Service rendez-vous non initialise.");
            return;
        }
        LocalDate date = datePicker.getValue();
        Integer heure = heureSpinner.getValue();
        Integer minute = minuteSpinner.getValue();
        Patient patient = patientCombo.getValue();
        Medecin medecin = medecinCombo.getValue();
        StatutRendezVous statut = statutCombo.getValue();

        if (date == null || patient == null || medecin == null) {
            showError("Date, patient et medecin sont obligatoires.");
            return;
        }

        LocalDateTime dateHeure = LocalDateTime.of(date, LocalTime.of(heure, minute));
        RendezVous selected = rendezVousTable.getSelectionModel().getSelectedItem();
        RendezVous rdv = (selected != null) ? selected : new RendezVous();
        rdv.setDateHeure(dateHeure);
        rdv.setPatient(patient);
        rdv.setMedecin(medecin);
        rdv.setStatut(statut == null ? StatutRendezVous.PROGRAMME : statut);

        try {
            rendezVousService.planifier(rdv);
            loadRendezVous();
            clearForm();
        } catch (IllegalStateException ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void onAnnuler() {
        RendezVous selected = rendezVousTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selectionnez un rendez-vous.");
            return;
        }
        rendezVousService.annuler(selected);
        loadRendezVous();
        clearForm();
    }

    private void loadData() {
        patientCombo.setItems(FXCollections.observableArrayList(patientService.findAll()));
        medecinCombo.setItems(FXCollections.observableArrayList(medecinService.findAll()));
        filtreMedecinCombo.setItems(FXCollections.observableArrayList(medecinService.findAll()));
        loadRendezVous();
    }

    private void loadRendezVous() {
        LocalDate date = filtreDatePicker.getValue();
        if (date == null) {
            date = LocalDate.now();
        }
        List<RendezVous> list = rendezVousService.rendezVousDuJour(date);
        rendezVousList.setAll(list);
    }

    private void fillForm(RendezVous rendezVous) {
        datePicker.setValue(rendezVous.getDateHeure().toLocalDate());
        heureSpinner.getValueFactory().setValue(rendezVous.getDateHeure().getHour());
        minuteSpinner.getValueFactory().setValue(rendezVous.getDateHeure().getMinute());
        patientCombo.setValue(rendezVous.getPatient());
        medecinCombo.setValue(rendezVous.getMedecin());
        statutCombo.setValue(rendezVous.getStatut());
    }

    private void clearForm() {
        datePicker.setValue(null);
        heureSpinner.getValueFactory().setValue(9);
        minuteSpinner.getValueFactory().setValue(0);
        patientCombo.setValue(null);
        medecinCombo.setValue(null);
        statutCombo.setValue(null);
        rendezVousTable.getSelectionModel().clearSelection();
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

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}