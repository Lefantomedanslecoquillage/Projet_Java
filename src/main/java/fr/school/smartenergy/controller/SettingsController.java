package fr.school.smartenergy.controller;

import fr.school.smartenergy.model.EnergyType;
import fr.school.smartenergy.service.CostService;
import fr.school.smartenergy.service.SettingsService;
import fr.school.smartenergy.util.DialogUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Map;

public class SettingsController {

    @FXML private ComboBox<String> comboTheme;
    @FXML private TextField fieldCurrency;
    @FXML private TextField fieldCity;
    @FXML private GridPane gridTariffs;

    private final SettingsService settingsService = new SettingsService();
    private final CostService costService = new CostService();

    @FXML
    public void initialize() {
        comboTheme.getItems().addAll("light", "dark");
        comboTheme.setValue(settingsService.getTheme());
        fieldCurrency.setText(settingsService.getCurrency());
        fieldCity.setText(settingsService.getDefaultCity());
        loadTariffs();
    }

    private void loadTariffs() {
        gridTariffs.getChildren().clear();
        Map<EnergyType, Double> tariffs = costService.getTariffs();
        int row = 0;
        for (Map.Entry<EnergyType, Double> entry : tariffs.entrySet()) {
            Label label = new Label(entry.getKey().getLabel());
            Label value = new Label(String.format("%.4f €/%s", entry.getValue(), entry.getKey().getUnit()));
            gridTariffs.add(label, 0, row);
            gridTariffs.add(value, 1, row);
            row++;
        }
    }

    @FXML
    public void onSave() {
        settingsService.setTheme(comboTheme.getValue());
        settingsService.setCurrency(fieldCurrency.getText());
        settingsService.setDefaultCity(fieldCity.getText());
        try {
            settingsService.save();
            DialogUtils.showInfo("Paramètres", "Paramètres sauvegardés.");
        } catch (Exception e) {
            DialogUtils.showError("Erreur", "Impossible de sauvegarder : " + e.getMessage());
        }
    }
}

