package fr.school.smartenergy.controller;

import fr.school.smartenergy.model.Building;
import fr.school.smartenergy.model.EnergyType;
import fr.school.smartenergy.service.BuildingService;
import fr.school.smartenergy.service.ConsumptionService;
import fr.school.smartenergy.util.DialogUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ConsumptionFormController {

    @FXML private ComboBox<Building> comboBuilding;
    @FXML private DatePicker datePicker;
    @FXML private TextField fieldTime;
    @FXML private ComboBox<EnergyType> comboEnergyType;
    @FXML private TextField fieldQuantity;

    private Runnable onSave;

    private final ConsumptionService consumptionService = new ConsumptionService();
    private final BuildingService buildingService = new BuildingService();

    @FXML
    public void initialize() {
        List<Building> buildings = buildingService.getAllBuildings();
        comboBuilding.getItems().addAll(buildings);
        comboBuilding.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Building b) { return b == null ? "" : b.getName(); }
            @Override public Building fromString(String s) { return null; }
        });
        comboEnergyType.getItems().addAll(EnergyType.values());
        datePicker.setValue(LocalDate.now());
        fieldTime.setText(LocalTime.now().withSecond(0).withNano(0).toString());
    }

    public void setOnSave(Runnable onSave) {
        this.onSave = onSave;
    }

    @FXML
    public void onSave() {
        Building building = comboBuilding.getValue();
        LocalDate date = datePicker.getValue();
        String timeText = fieldTime.getText();
        EnergyType energyType = comboEnergyType.getValue();
        String quantityText = fieldQuantity.getText();

        if (building == null) {
            DialogUtils.showError("Validation", "Veuillez sélectionner un bâtiment.");
            return;
        }
        if (date == null) {
            DialogUtils.showError("Validation", "Veuillez sélectionner une date.");
            return;
        }
        if (energyType == null) {
            DialogUtils.showError("Validation", "Veuillez sélectionner un type d'énergie.");
            return;
        }

        LocalTime time;
        try {
            time = LocalTime.parse(timeText);
        } catch (Exception e) {
            DialogUtils.showError("Validation", "Format d'heure invalide (attendu : HH:mm).");
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
            if (quantity < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            DialogUtils.showError("Validation", "La quantité doit être un nombre positif ou nul.");
            return;
        }

        consumptionService.add(building.getId(), date, time, energyType, quantity);
        if (onSave != null) onSave.run();
    }

    @FXML
    public void onCancel() {
        comboBuilding.getScene().getWindow().hide();
    }
}
