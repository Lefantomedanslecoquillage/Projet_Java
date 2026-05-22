package fr.school.smartenergy.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane rootPane;

    public void loadView(String fxml) {
        try {
            BorderPane view = FXMLLoader.load(getClass().getResource("/fr/smartenergy/view/" + fxml));
            rootPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void showAnalysis() {
        loadView("analysis.fxml");
    }

    @FXML
    public void showSettings() {
        loadView("settings.fxml");
    }
}
