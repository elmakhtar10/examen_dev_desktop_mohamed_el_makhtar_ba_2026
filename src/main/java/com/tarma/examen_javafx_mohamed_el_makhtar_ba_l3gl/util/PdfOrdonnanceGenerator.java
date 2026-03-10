package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.util;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model.Consultation;

import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

public final class PdfOrdonnanceGenerator {

    private PdfOrdonnanceGenerator() {
    }

    public static void generate(Consultation consultation, File file) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        document.add(new Paragraph("Clinique Medicale - Ordonnance", titleFont));
        document.add(new Paragraph(" "));

        String patient = consultation.getPatient().getNom() + " " + consultation.getPatient().getPrenom();
        String medecin = consultation.getMedecin().getNom() + " " + consultation.getMedecin().getPrenom();
        String date = consultation.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        document.add(new Paragraph("Patient: " + patient, normalFont));
        document.add(new Paragraph("Medecin: " + medecin, normalFont));
        document.add(new Paragraph("Date: " + date, normalFont));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Diagnostic:", titleFont));
        document.add(new Paragraph(nullToEmpty(consultation.getDiagnostic()), normalFont));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Observations:", titleFont));
        document.add(new Paragraph(nullToEmpty(consultation.getObservations()), normalFont));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Prescription:", titleFont));
        document.add(new Paragraph(nullToEmpty(consultation.getPrescription()), normalFont));

        document.close();
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}