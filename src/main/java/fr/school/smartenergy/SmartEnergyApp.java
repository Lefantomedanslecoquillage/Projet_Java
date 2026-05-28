package fr.school.smartenergy;

import fr.school.smartenergy.dao.DatabaseManager;
import fr.school.smartenergy.service.TestDataService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class SmartEnergyApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the database (creates tables if needed)
        DatabaseManager.getInstance();

        // Generate sample data only if the database is empty
        new TestDataService().generateSampleData();

        URL fxmlUrl = getClass().getResource("view/main.fxml");
        if (fxmlUrl == null) {
            throw new IllegalStateException("Impossible de trouver main.fxml");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load(), 1100, 700);

        URL cssUrl = getClass().getResource("css/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        primaryStage.setTitle("Smart Energy Manager");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    @Override
    public void stop() {
        DatabaseManager.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

