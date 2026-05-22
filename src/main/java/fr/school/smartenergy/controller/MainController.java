package fr.school.smartenergy.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public class MainController {

    @FXML
    private BorderPane rootPane;

    @FXML
    public void initialize() {
        showDashboard();
    }

    @FXML
    public void showDashboard() {
        loadView("dashboard.fxml");
    }

    @FXML
    public void showBuildings() {
        loadView("buildings.fxml");
    }

    @FXML
    public void showConsumptions() {
        loadView("consumptions.fxml");
    }

    @FXML
    public void showCharts() {
        loadView("charts.fxml");
    }

    @FXML
    public void showAnalysis() {
        loadView("analysis.fxml");
    }

    @FXML
    public void showSettings() {
        loadView("settings.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            URL url = getClass().getResource("/fr/school/smartenergy/view/" + fxmlFile);
            if (url == null) {
                System.err.println("Vue introuvable : " + fxmlFile);
                return;
            }
            Pane view = FXMLLoader.load(url);
            rootPane.setCenter(view);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de " + fxmlFile + " : " + e.getMessage());
        }
    }
}

