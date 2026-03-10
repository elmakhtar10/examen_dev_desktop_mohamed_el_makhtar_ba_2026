package com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl;

import com.tarma.examen_javafx_mohamed_el_makhtar_ba_l3gl.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tarma/examen_javafx_mohamed_el_makhtar_ba_l3gl/views/login-view.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        LoginController controller = loader.getController();
        controller.setStage(primaryStage);

        primaryStage.setTitle("Gestion Clinique - Connexion");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

