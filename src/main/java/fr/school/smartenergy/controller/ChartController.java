package fr.school.smartenergy.controller;

import fr.school.smartenergy.service.ChartService;
import javafx.fxml.FXML;
import javafx.scene.chart.*;

import java.time.LocalDate;
import java.util.Map;

public class ChartController {

    @FXML private LineChart<String, Number> lineChart;
    @FXML private BarChart<String, Number> barChart;
    @FXML private PieChart pieChart;

    private final ChartService chartService = new ChartService();

    @FXML
    public void initialize() {
        loadLineChart();
        loadBarChart();
        loadPieChart();
    }

    private void loadLineChart() {
        Map<String, Double> data = chartService.getDailyEvolution(30);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Consommation journalière (30 jours)");
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        lineChart.getData().clear();
        lineChart.getData().add(series);
    }

    private void loadBarChart() {
        Map<String, Double> data = chartService.getConsumptionPerBuilding();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Consommation par bâtiment (" + LocalDate.now().getYear() + ")");
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChart.getData().clear();
        barChart.getData().add(series);
    }

    private void loadPieChart() {
        Map<String, Double> data = chartService.getConsumptionPerEnergyType();
        pieChart.getData().clear();
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        pieChart.setTitle("Répartition par type d'énergie");
    }
}

