package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.controller;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Consultation;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Facture;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.StatutFacture;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.ConsultationService;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.service.FactureService;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.util.PdfFactureGenerator;
import jakarta.persistence.EntityManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class FacturesController {

    @FXML
    private TableView<Facture> facturesTable;
    @FXML
    private TableColumn<Facture, String> patientColumn;
    @FXML
    private TableColumn<Facture, String> dateColumn;
    @FXML
    private TableColumn<Facture, String> montantColumn;
    @FXML
    private TableColumn<Facture, String> statutColumn;

    @FXML
    private ComboBox<Consultation> consultationCombo;
    @FXML
    private TextField montantField;
    @FXML
    private ComboBox<String> modePaiementCombo;
    @FXML
    private ComboBox<StatutFacture> statutCombo;

    private EntityManager em;
    private FactureService factureService;
    private ConsultationService consultationService;

    private final ObservableList<Facture> factures = FXCollections.observableArrayList();

    public void setContext(EntityManager em, FactureService factureService, ConsultationService consultationService) {
        this.em = em;
        this.factureService = factureService;
        this.consultationService = consultationService;
        loadData();
    }

    @FXML
    private void initialize() {
        patientColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getConsultation().getPatient().getNom() + " "
                        + data.getValue().getConsultation().getPatient().getPrenom()));
        dateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getConsultation().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        montantColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getMontantTotal().toPlainString()));
        statutColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getStatut().name()));

        modePaiementCombo.setItems(FXCollections.observableArrayList("ESPECES", "CARTE", "CHEQUE"));
        statutCombo.setItems(FXCollections.observableArrayList(StatutFacture.values()));

        setupConsultationDisplay();

        facturesTable.setItems(factures);
    }

    @FXML
    private void onGenerer() {
        Consultation consultation = consultationCombo.getValue();
        if (consultation == null) {
            showError("Selectionnez une consultation.");
            return;
        }
        BigDecimal montant;
        try {
            montant = new BigDecimal(montantField.getText());
        } catch (Exception ex) {
            showError("Montant invalide.");
            return;
        }
        String modePaiement = modePaiementCombo.getValue();
        if (modePaiement == null || modePaiement.isBlank()) {
            showError("Mode de paiement obligatoire.");
            return;
        }
        StatutFacture statut = statutCombo.getValue();

        factureService.genererFacture(consultation, montant, modePaiement, statut);
        loadFactures();
    }

    @FXML
    private void onExporterPdf() {
        Facture selected = facturesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selectionnez une facture.");
            return;
        }
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Exporter facture");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        chooser.setInitialFileName("facture_" + selected.getId() + ".pdf");
        File file = chooser.showSaveDialog(facturesTable.getScene().getWindow());
        if (file == null) {
            return;
        }
        try {
            PdfFactureGenerator.generate(selected, file);
        } catch (Exception ex) {
            showError("Erreur lors de la generation du PDF : " + ex.getMessage());
        }
    }

    private void loadData() {
        consultationCombo.setItems(FXCollections.observableArrayList(consultationService.findAll()));
        loadFactures();
    }

    private void loadFactures() {
        factures.setAll(factureService.findAll());
    }

    private void setupConsultationDisplay() {
        consultationCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Consultation item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String text = item.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            + " - " + item.getPatient().getNom() + " " + item.getPatient().getPrenom();
                    setText(text);
                }
            }
        });
        consultationCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Consultation item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String text = item.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
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