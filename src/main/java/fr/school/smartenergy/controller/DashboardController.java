package fr.school.smartenergy.controller;

import fr.school.smartenergy.model.Alert;
import fr.school.smartenergy.model.DashboardStats;
import fr.school.smartenergy.model.WeatherData;
import fr.school.smartenergy.service.DashboardService;
import fr.school.smartenergy.service.WeatherService;
import fr.school.smartenergy.exception.WeatherException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class DashboardController {

    @FXML private Label labelDailyTotal;
    @FXML private Label labelMonthlyTotal;
    @FXML private Label labelYearlyTotal;
    @FXML private Label labelTotalCost;
    @FXML private Label labelTopBuilding;
    @FXML private ListView<String> listAlerts;

    @FXML private Label labelWeatherTemperature;
    @FXML private Label labelWeatherHumidity;
    @FXML private Label labelWeatherWind;
    @FXML private Label labelWeatherPrecipitation;
    @FXML private Label labelWeatherDescription;
    @FXML private Label labelWeatherAdvice;

    private final DashboardService dashboardService = new DashboardService();
    private final WeatherService weatherService = new WeatherService();

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

        loadWeather();
    }

    private void loadWeather() {
        setWeatherLoadingState();

        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        // Coordonnées par défaut : Issy-les-Moulineaux / Paris proche.
                        // Plus tard, on pourra les lire depuis les paramètres.
                        return weatherService.getCurrentWeather(48.8245, 2.2743);
                    } catch (WeatherException e) {
                        throw new CompletionException(e);
                    }
                })
                .thenAccept(weather -> Platform.runLater(() -> updateWeatherLabels(weather)))
                .exceptionally(error -> {
                    Platform.runLater(this::setWeatherErrorState);
                    return null;
                });
    }

    private void updateWeatherLabels(WeatherData weather) {
        labelWeatherTemperature.setText(String.format("Température : %.1f °C", weather.getTemperature()));
        labelWeatherHumidity.setText(String.format("Humidité : %.0f %%", weather.getHumidity()));
        labelWeatherWind.setText(String.format("Vent : %.1f km/h", weather.getWindSpeed()));
        labelWeatherPrecipitation.setText(String.format("Précipitations : %.1f mm", weather.getPrecipitation()));
        labelWeatherDescription.setText("État : " + weather.getWeatherDescription());
        labelWeatherAdvice.setText("Conseil : " + weather.getEnergyAdvice());
    }

    private void setWeatherLoadingState() {
        labelWeatherTemperature.setText("Température : chargement...");
        labelWeatherHumidity.setText("Humidité : chargement...");
        labelWeatherWind.setText("Vent : chargement...");
        labelWeatherPrecipitation.setText("Précipitations : chargement...");
        labelWeatherDescription.setText("État : chargement...");
        labelWeatherAdvice.setText("Conseil : analyse en cours...");
    }

    private void setWeatherErrorState() {
        labelWeatherTemperature.setText("Température : indisponible");
        labelWeatherHumidity.setText("Humidité : indisponible");
        labelWeatherWind.setText("Vent : indisponible");
        labelWeatherPrecipitation.setText("Précipitations : indisponible");
        labelWeatherDescription.setText("État : météo indisponible");
        labelWeatherAdvice.setText("Conseil : impossible d'analyser la météo actuellement.");
    }
}