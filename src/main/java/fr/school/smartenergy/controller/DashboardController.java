package fr.school.smartenergy.controller;

import fr.school.smartenergy.model.Alert;
import fr.school.smartenergy.model.DashboardStats;
import fr.school.smartenergy.service.DashboardService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DashboardController {

    @FXML private Label labelDailyTotal;
    @FXML private Label labelMonthlyTotal;
    @FXML private Label labelYearlyTotal;
    @FXML private Label labelTotalCost;
    @FXML private Label labelTopBuilding;
    @FXML private ListView<String> listAlerts;

    private final DashboardService dashboardService = new DashboardService();

    @FXML
    public void initialize() {
        refresh();
    }

    @FXML
    public void refresh() {
        DashboardStats stats = dashboardService.computeStats();

        labelDailyTotal.setText(String.format("%.2f unités", stats.getDailyTotal()));
        labelMonthlyTotal.setText(String.format("%.2f unités", stats.getMonthlyTotal()));
        labelYearlyTotal.setText(String.format("%.2f unités", stats.getYearlyTotal()));
        labelTotalCost.setText(String.format("%.2f €", stats.getTotalCost()));
        labelTopBuilding.setText(stats.getTopBuildingName());

        listAlerts.getItems().clear();
        if (stats.getAlerts() != null) {
            for (Alert a : stats.getAlerts()) {
                listAlerts.getItems().add(a.toString());
            }
        }
    }
}

