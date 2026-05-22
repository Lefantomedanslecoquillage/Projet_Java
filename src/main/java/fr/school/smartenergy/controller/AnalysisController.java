package fr.school.smartenergy.controller;

import fr.school.smartenergy.model.Alert;
import fr.school.smartenergy.model.Building;
import fr.school.smartenergy.model.ConsumptionRecord;
import fr.school.smartenergy.model.EnergyType;
import fr.school.smartenergy.service.AnalysisService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;

public class AnalysisController {

    @FXML private Label labelTopBuilding;
    @FXML private Label labelDominantType;
    @FXML private Label labelMonthlyBill;
    @FXML private ListView<String> listPeaks;
    @FXML private ListView<String> listAlerts;

    private final AnalysisService analysisService = new AnalysisService();

    @FXML
    public void initialize() {
        refresh();
    }

    @FXML
    public void refresh() {
        Building top = analysisService.findTopBuilding();
        labelTopBuilding.setText(top != null ? top.getName() : "Aucune donnée");

        EnergyType dominant = analysisService.findDominantEnergyType();
        labelDominantType.setText(dominant != null ? dominant.getLabel() : "Aucune donnée");

        double bill = analysisService.estimateMonthlyBill();
        labelMonthlyBill.setText(String.format("%.2f €", bill));

        List<ConsumptionRecord> peaks = analysisService.detectPeaks();
        listPeaks.getItems().clear();
        for (ConsumptionRecord r : peaks) {
            listPeaks.getItems().add(
                r.getDate() + " | " + r.getEnergyType().getLabel() + " | " +
                String.format("%.2f", r.getQuantity()) + " " + r.getEnergyType().getUnit()
            );
        }

        List<Alert> alerts = analysisService.generateAlerts();
        listAlerts.getItems().clear();
        for (Alert a : alerts) {
            listAlerts.getItems().add(a.toString());
        }
    }
}

