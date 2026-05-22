package fr.school.smartenergy.controller;

import fr.school.smartenergy.model.Building;
import fr.school.smartenergy.model.ConsumptionRecord;
import fr.school.smartenergy.model.EnergyType;
import fr.school.smartenergy.service.BuildingService;
import fr.school.smartenergy.service.ConsumptionService;
import fr.school.smartenergy.util.DialogUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

public class ConsumptionController {

    @FXML private TableView<ConsumptionRecord> tableRecords;
    @FXML private TableColumn<ConsumptionRecord, Integer> colId;
    @FXML private TableColumn<ConsumptionRecord, Integer> colBuildingId;
    @FXML private TableColumn<ConsumptionRecord, String> colDate;
    @FXML private TableColumn<ConsumptionRecord, String> colTime;
    @FXML private TableColumn<ConsumptionRecord, EnergyType> colEnergyType;
    @FXML private TableColumn<ConsumptionRecord, Double> colQuantity;
    @FXML private TableColumn<ConsumptionRecord, Double> colCost;
    @FXML private ComboBox<Building> filterBuilding;
    @FXML private ComboBox<String> filterEnergyType;

    private final ConsumptionService consumptionService = new ConsumptionService();
    private final BuildingService buildingService = new BuildingService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBuildingId.setCellValueFactory(new PropertyValueFactory<>("buildingId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colEnergyType.setCellValueFactory(new PropertyValueFactory<>("energyType"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("estimatedCost"));

        // Populate building filter
        List<Building> buildings = buildingService.getAllBuildings();
        filterBuilding.getItems().add(null); // "All" option
        filterBuilding.getItems().addAll(buildings);
        filterBuilding.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Building b) { return b == null ? "Tous" : b.getName(); }
            @Override public Building fromString(String s) { return null; }
        });

        // Populate energy type filter
        filterEnergyType.getItems().add("Tous");
        for (EnergyType t : EnergyType.values()) filterEnergyType.getItems().add(t.name());
        filterEnergyType.setValue("Tous");

        loadRecords();
    }

    @FXML
    public void onFilter() {
        loadRecords();
    }

    private void loadRecords() {
        Building selectedBuilding = filterBuilding.getValue();
        String selectedType = filterEnergyType.getValue();

        List<ConsumptionRecord> records;
        if (selectedBuilding != null) {
            records = consumptionService.getByBuilding(selectedBuilding.getId());
        } else {
            records = consumptionService.getAllRecords();
        }

        // Filter by energy type if not "Tous"
        if (selectedType != null && !selectedType.equals("Tous")) {
            EnergyType type = EnergyType.valueOf(selectedType);
            records = records.stream().filter(r -> r.getEnergyType() == type).toList();
        }

        tableRecords.setItems(FXCollections.observableArrayList(records));
    }

    @FXML
    public void onAdd() {
        openForm(null);
    }

    @FXML
    public void onDelete() {
        ConsumptionRecord selected = tableRecords.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogUtils.showWarning("Aucune sélection", "Veuillez sélectionner un relevé.");
            return;
        }
        boolean confirmed = DialogUtils.showConfirmation("Supprimer", "Supprimer ce relevé ?");
        if (confirmed) {
            consumptionService.delete(selected.getId());
            loadRecords();
        }
    }

    @FXML
    public void onImportCsv() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Importer un fichier CSV");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = chooser.showOpenDialog(tableRecords.getScene().getWindow());
        if (file == null) return;

        try {
            Path path = file.toPath();
            int count = consumptionService.importFromCsv(path);
            loadRecords();
            DialogUtils.showInfo("Import CSV", count + " relevés importés avec succès.");
        } catch (Exception e) {
            DialogUtils.showError("Erreur d'import", e.getMessage());
        }
    }

    private void openForm(ConsumptionRecord record) {
        try {
            URL url = getClass().getResource("/fr/school/smartenergy/view/consumption-form.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Nouveau relevé");
            dialog.setScene(new Scene(loader.load()));

            ConsumptionFormController ctrl = loader.getController();
            ctrl.setOnSave(() -> {
                loadRecords();
                dialog.close();
            });

            dialog.showAndWait();
        } catch (IOException e) {
            DialogUtils.showError("Erreur", "Impossible d'ouvrir le formulaire : " + e.getMessage());
        }
    }
}

