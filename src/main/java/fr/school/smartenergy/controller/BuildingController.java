package fr.school.smartenergy.controller;

import fr.school.smartenergy.model.Building;
import fr.school.smartenergy.model.BuildingType;
import fr.school.smartenergy.service.BuildingService;
import fr.school.smartenergy.util.DialogUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class BuildingController {

    @FXML private TableView<Building> tableBuildings;
    @FXML private TableColumn<Building, Integer> colId;
    @FXML private TableColumn<Building, String> colName;
    @FXML private TableColumn<Building, BuildingType> colType;
    @FXML private TableColumn<Building, String> colAddress;
    @FXML private TableColumn<Building, Double> colSurface;
    @FXML private TableColumn<Building, Integer> colOccupants;

    private final BuildingService buildingService = new BuildingService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSurface.setCellValueFactory(new PropertyValueFactory<>("surface"));
        colOccupants.setCellValueFactory(new PropertyValueFactory<>("occupants"));
        loadBuildings();
    }

    private void loadBuildings() {
        List<Building> buildings = buildingService.getAllBuildings();
        tableBuildings.setItems(FXCollections.observableArrayList(buildings));
    }

    @FXML
    public void onAdd() {
        openForm(null);
    }

    @FXML
    public void onEdit() {
        Building selected = tableBuildings.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogUtils.showWarning("Aucune sélection", "Veuillez sélectionner un bâtiment.");
            return;
        }
        openForm(selected);
    }

    @FXML
    public void onDelete() {
        Building selected = tableBuildings.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogUtils.showWarning("Aucune sélection", "Veuillez sélectionner un bâtiment.");
            return;
        }
        boolean confirmed = DialogUtils.showConfirmation("Supprimer",
            "Supprimer le bâtiment \"" + selected.getName() + "\" et toutes ses consommations ?");
        if (confirmed) {
            buildingService.delete(selected.getId());
            loadBuildings();
        }
    }

    @FXML
    public void onClone() {
        Building selected = tableBuildings.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogUtils.showWarning("Aucune sélection", "Veuillez sélectionner un bâtiment.");
            return;
        }
        buildingService.clone(selected.getId());
        loadBuildings();
        DialogUtils.showInfo("Clonage", "Bâtiment cloné avec succès.");
    }

    private void openForm(Building building) {
        try {
            URL url = getClass().getResource("/fr/school/smartenergy/view/building-form.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle(building == null ? "Nouveau bâtiment" : "Modifier le bâtiment");
            dialog.setScene(new Scene(loader.load()));

            BuildingFormController formController = loader.getController();
            formController.setBuilding(building);
            formController.setOnSave(() -> {
                loadBuildings();
                dialog.close();
            });

            dialog.showAndWait();
        } catch (IOException e) {
            DialogUtils.showError("Erreur", "Impossible d'ouvrir le formulaire : " + e.getMessage());
        }
    }
}

