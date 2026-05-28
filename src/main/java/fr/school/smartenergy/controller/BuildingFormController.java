package fr.school.smartenergy.controller;

import fr.school.smartenergy.model.Building;
import fr.school.smartenergy.model.BuildingType;
import fr.school.smartenergy.service.BuildingService;
import fr.school.smartenergy.util.DialogUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class BuildingFormController {

    @FXML private TextField fieldName;
    @FXML private ComboBox<BuildingType> comboType;
    @FXML private TextField fieldAddress;
    @FXML private TextField fieldSurface;
    @FXML private TextField fieldOccupants;

    private Building building;
    private Runnable onSave;

    private final BuildingService buildingService = new BuildingService();

    @FXML
    public void initialize() {
        comboType.getItems().setAll(BuildingType.values());
    }

    public void setBuilding(Building building) {
        this.building = building;
        if (building != null) {
            fieldName.setText(building.getName());
            comboType.setValue(building.getType());
            fieldAddress.setText(building.getAddress());
            fieldSurface.setText(String.valueOf(building.getSurface()));
            fieldOccupants.setText(String.valueOf(building.getOccupants()));
        }
    }

    public void setOnSave(Runnable onSave) {
        this.onSave = onSave;
    }

    @FXML
    public void onSave() {
        String name = fieldName.getText();
        BuildingType type = comboType.getValue();
        String address = fieldAddress.getText();
        String surfaceText = fieldSurface.getText();
        String occupantsText = fieldOccupants.getText();

        if (name == null || name.isBlank()) {
            DialogUtils.showError("Validation", "Le nom est obligatoire.");
            return;
        }
        if (type == null) {
            DialogUtils.showError("Validation", "Le type est obligatoire.");
            return;
        }
        double surface;
        int occupants;
        try {
            surface = Double.parseDouble(surfaceText);
            if (surface <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            DialogUtils.showError("Validation", "La surface doit être un nombre positif.");
            return;
        }
        try {
            occupants = Integer.parseInt(occupantsText);
            if (occupants <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            DialogUtils.showError("Validation", "Le nombre d'occupants doit être un entier positif.");
            return;
        }

        if (building == null) {
            building = new Building(name, type, address, surface, occupants);
            buildingService.create(building);
        } else {
            building.setName(name);
            building.setType(type);
            building.setAddress(address);
            building.setSurface(surface);
            building.setOccupants(occupants);
            buildingService.update(building);
        }

        if (onSave != null) onSave.run();
    }

    @FXML
    public void onCancel() {
        fieldName.getScene().getWindow().hide();
    }
}
