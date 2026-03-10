package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.util;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Facture;

import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

public final class PdfFactureGenerator {

    private PdfFactureGenerator() {
    }

    public static void generate(Facture facture, File file) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        document.add(new Paragraph("Clinique Medicale - Facture", titleFont));
        document.add(new Paragraph(" "));

        String patient = facture.getConsultation().getPatient().getNom() + " "
                + facture.getConsultation().getPatient().getPrenom();
        String date = facture.getConsultation().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        document.add(new Paragraph("Patient: " + patient, normalFont));
        document.add(new Paragraph("Date consultation: " + date, normalFont));
        document.add(new Paragraph("Montant: " + facture.getMontantTotal().toPlainString(), normalFont));
        document.add(new Paragraph("Mode de paiement: " + facture.getModePaiement(), normalFont));
        document.add(new Paragraph("Statut: " + facture.getStatut().name(), normalFont));

        document.close();
    }
}