module com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    // JPA / Hibernate
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    // PDF (OpenPDF)
    requires com.github.librepdf.openpdf;
    requires static lombok;

    // JavaFX controllers
    opens com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl to javafx.fxml;
    opens com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.controller to javafx.fxml;

    // JPA entities (Hibernate uses reflection)
    opens com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.model;

    exports com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl;
}